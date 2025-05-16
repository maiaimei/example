package org.example.openapi;

import static org.example.openapi.OpenAPIConstants.COMPONENTS_SCHEMAS_PATH;
import static org.example.openapi.OpenAPIType.DOUBLE;
import static org.example.openapi.OpenAPIType.FLOAT;
import static org.example.openapi.OpenAPIType.INT64;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * A utility class for generating OpenAPI schemas from annotated Java classes. This class scans a specified package for classes
 * annotated with {@link Schema}, processes their fields, and generates OpenAPI-compatible schemas.
 */
public class OpenAPIModelSchemaGenerator {

  private final OpenAPI openAPI;
  private final Map<Class<?>, Map<String, Object>> processedExamples = new HashMap<>();
  private final Set<Class<?>> processedClasses = new HashSet<>();
  private final Components processedComponents = new Components();

  /**
   * Constructor to initialize the generator with an OpenAPI instance.
   *
   * @param openAPI The OpenAPI instance to which schemas will be added.
   */
  public OpenAPIModelSchemaGenerator(OpenAPI openAPI) {
    this.openAPI = openAPI;
  }

  /**
   * Scans the specified package for classes annotated with {@link Schema}, processes them, and generates OpenAPI schemas.
   *
   * @param basePackage The base package to scan for annotated classes.
   */
  public void generateSchemas(String basePackage) {
    ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(Schema.class));

    for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
      try {
        Class<?> modelClass = ClassUtils.forName(Objects.requireNonNull(bd.getBeanClassName()),
            this.getClass().getClassLoader());
        processClass(modelClass);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException("Failed to load class: " + bd.getBeanClassName(), e);
      }
    }
  }

  /**
   * Processes a single class to generate its OpenAPI schema. This method handles the class's fields, annotations, and
   * relationships.
   *
   * @param modelClass The class to process.
   */
  public void processClass(Class<?> modelClass) {
    String modelName = modelClass.getSimpleName();

    if (processedClasses.contains(modelClass)) {
      return;
    }
    processedClasses.add(modelClass);

    if (schemaAlreadyExists(modelName)) {
      return;
    }

    Schema classSchema = modelClass.getAnnotation(Schema.class);
    ObjectSchema schema = new ObjectSchema();

    if (classSchema != null && StringUtils.hasText(classSchema.description())) {
      schema.description(classSchema.description());
    }

    Map<String, io.swagger.v3.oas.models.media.Schema> properties = new LinkedHashMap<>();
    List<String> requiredFields = new ArrayList<>();
    Map<String, Object> exampleValues = new LinkedHashMap<>();

    for (Field field : modelClass.getDeclaredFields()) {
      Schema fieldSchema = field.getAnnotation(Schema.class);
      if (fieldSchema != null) {
        processField(field, fieldSchema, properties, requiredFields, exampleValues);
      }
    }

    schema.setProperties(properties);
    if (!requiredFields.isEmpty()) {
      schema.setRequired(requiredFields);
    }

    ensureComponentsExist();
    openAPI.getComponents().addSchemas(modelName, schema);
    processedComponents.addSchemas(modelName, schema);

    if (!exampleValues.isEmpty()) {
      processedExamples.put(modelClass, exampleValues);
    }
  }

  /**
   * Processes a single field of a class to generate its schema and example values.
   *
   * @param field          The field to process.
   * @param fieldSchema    The {@link Schema} annotation on the field.
   * @param properties     The map of properties to which the field's schema will be added.
   * @param requiredFields The list of required fields to which the field will be added if required.
   * @param exampleValues  The map of example values to which the field's example will be added.
   */
  private void processField(Field field, Schema fieldSchema,
      Map<String, io.swagger.v3.oas.models.media.Schema> properties,
      List<String> requiredFields,
      Map<String, Object> exampleValues) {
    io.swagger.v3.oas.models.media.Schema<?> propertySchema = createSchemaFromType(field.getGenericType());

    if (StringUtils.hasText(fieldSchema.description())) {
      propertySchema.description(fieldSchema.description());
    }

    if (fieldSchema.requiredMode() == Schema.RequiredMode.REQUIRED) {
      requiredFields.add(field.getName());
    }

    if (StringUtils.hasText(fieldSchema.example())) {
      Object example = convertExample(fieldSchema.example(), field.getType());
      propertySchema.example(example);
      exampleValues.put(field.getName(), example);
    }

    properties.put(field.getName(), propertySchema);
  }

  /**
   * Creates a schema for a given Java type.
   *
   * @param type The Java type to process.
   * @return The generated schema.
   */
  private io.swagger.v3.oas.models.media.Schema<?> createSchemaFromType(Type type) {
    if (type instanceof Class<?> clazz) {
      return createSchemaForClass(clazz);
    } else if (type instanceof ParameterizedType parameterizedType) {
      return handleParameterizedType(parameterizedType);
    } else if (type instanceof WildcardType wildcardType) {
      return createSchemaFromType(wildcardType.getUpperBounds()[0]);
    } else if (type instanceof TypeVariable<?> typeVar) {
      return createSchemaFromType(typeVar.getBounds()[0]);
    }
    return new ObjectSchema();
  }

  /**
   * Creates a schema for a given Java class.
   *
   * @param clazz The Java class to process.
   * @return The generated schema.
   */
  private io.swagger.v3.oas.models.media.Schema<?> createSchemaForClass(Class<?> clazz) {
    if (ClassUtils.isPrimitiveOrWrapper(clazz)) {
      return createPrimitiveSchema(clazz);
    }

    if (clazz.isEnum()) {
      return createEnumSchema(clazz);
    }

    if (Collection.class.isAssignableFrom(clazz)) {
      return new ArraySchema().items(new ObjectSchema());
    }

    if (Map.class.isAssignableFrom(clazz)) {
      return new MapSchema().additionalProperties(new ObjectSchema());
    }

    if (clazz.isArray()) {
      return new ArraySchema().items(createSchemaForClass(clazz.getComponentType()));
    }

    if (!clazz.isPrimitive() && !clazz.getPackage().getName().startsWith("java.")) {
      processClass(clazz);
      return new io.swagger.v3.oas.models.media.Schema<>().$ref(COMPONENTS_SCHEMAS_PATH + clazz.getSimpleName());
    }

    return new ObjectSchema();
  }

  /**
   * Handles parameterized types (e.g., collections, maps) to generate their schemas.
   *
   * @param parameterizedType The parameterized type to process.
   * @return The generated schema.
   */
  private io.swagger.v3.oas.models.media.Schema<?> handleParameterizedType(ParameterizedType parameterizedType) {
    Class<?> rawType = (Class<?>) parameterizedType.getRawType();
    Type[] typeArguments = parameterizedType.getActualTypeArguments();

    if (Collection.class.isAssignableFrom(rawType)) {
      return new ArraySchema().items(createSchemaFromType(typeArguments[0]));
    }

    if (Map.class.isAssignableFrom(rawType)) {
      return new MapSchema().additionalProperties(createSchemaFromType(typeArguments[1]));
    }

    processClass(rawType);
    return new io.swagger.v3.oas.models.media.Schema<>().$ref(COMPONENTS_SCHEMAS_PATH + rawType.getSimpleName());
  }

  /**
   * Creates a schema for a primitive or wrapper type.
   *
   * @param clazz The class representing the primitive or wrapper type.
   * @return The generated schema.
   */
  private io.swagger.v3.oas.models.media.Schema<?> createPrimitiveSchema(Class<?> clazz) {
    if (clazz == String.class) {
      return new StringSchema();
    } else if (clazz == Integer.class || clazz == int.class) {
      return new IntegerSchema();
    } else if (clazz == Long.class || clazz == long.class) {
      return new IntegerSchema().format(INT64);
    } else if (clazz == Double.class || clazz == double.class) {
      return new NumberSchema().format(DOUBLE);
    } else if (clazz == Float.class || clazz == float.class) {
      return new NumberSchema().format(FLOAT);
    } else if (clazz == Boolean.class || clazz == boolean.class) {
      return new BooleanSchema();
    } else if (clazz == BigDecimal.class) {
      return new NumberSchema();
    } else if (clazz == LocalDateTime.class) {
      return new StringSchema().format("date-time");
    }
    return new ObjectSchema();
  }

  /**
   * Creates a schema for an enum type.
   *
   * @param clazz The enum class to process.
   * @return The generated schema.
   */
  private io.swagger.v3.oas.models.media.Schema<?> createEnumSchema(Class<?> clazz) {
    StringSchema enumSchema = new StringSchema();
    enumSchema.setEnum(Arrays.stream(clazz.getEnumConstants())
        .map(Object::toString)
        .collect(Collectors.toList()));
    return enumSchema;
  }

  /**
   * Converts a string example value to the appropriate type.
   *
   * @param example The example value as a string.
   * @param type    The target type.
   * @return The converted example value.
   */
  private Object convertExample(String example, Class<?> type) {
    try {
      if (type == String.class) {
        return example;
      } else if (type == Integer.class || type == int.class) {
        return Integer.parseInt(example);
      } else if (type == Long.class || type == long.class) {
        return Long.parseLong(example);
      } else if (type == Double.class || type == double.class) {
        return Double.parseDouble(example);
      } else if (type == Float.class || type == float.class) {
        return Float.parseFloat(example);
      } else if (type == Boolean.class || type == boolean.class) {
        return Boolean.parseBoolean(example);
      } else if (type == BigDecimal.class) {
        return new BigDecimal(example);
      } else if (type == LocalDateTime.class) {
        return LocalDateTime.parse(example);
      }
    } catch (Exception e) {
      return example;
    }
    return example;
  }

  /**
   * Checks if a schema with the given model name already exists in the OpenAPI components.
   *
   * @param modelName The name of the model to check.
   * @return True if the schema already exists, false otherwise.
   */
  private boolean schemaAlreadyExists(String modelName) {
    return openAPI.getComponents() != null &&
        openAPI.getComponents().getSchemas() != null &&
        openAPI.getComponents().getSchemas().containsKey(modelName);
  }

  /**
   * Ensures that the OpenAPI components object is initialized. If it is null, a new Components object is created and set.
   */
  private void ensureComponentsExist() {
    if (openAPI.getComponents() == null) {
      openAPI.setComponents(new Components());
    }
  }

  /**
   * Retrieves the processed components object containing all schemas.
   *
   * @return The processed components object.
   */
  public Components getProcessedComponents() {
    return processedComponents;
  }

  /**
   * Retrieves a schema from the processed components by its key.
   *
   * @param key The key of the schema to retrieve.
   * @return The schema associated with the given key, or null if not found.
   */
  public io.swagger.v3.oas.models.media.Schema getSchema(String key) {
    return processedComponents.getSchemas().get(key);
  }

  /**
   * Retrieves all example values for all processed classes.
   *
   * @return A map where the key is the class and the value is a map of example values.
   */
  public Map<Class<?>, Map<String, Object>> getProcessedExamples() {
    return processedExamples;
  }

  /**
   * Retrieves example values for a specific schema key.
   *
   * @param clazz The clazz of the schema for which to retrieve examples.
   * @return A map of example values for the schema, or null if not found.
   */
  public Map<String, Object> getExample(Class<?> clazz) {
    return processedExamples.get(clazz);
  }
}