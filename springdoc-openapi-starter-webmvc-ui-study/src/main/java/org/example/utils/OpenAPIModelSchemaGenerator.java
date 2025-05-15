package org.example.utils;

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

public class OpenAPIModelSchemaGenerator {

  private final OpenAPI openAPI;
  private final Map<Class<?>, Object> examples = new HashMap<>();
  private final Set<Class<?>> processedClasses = new HashSet<>();

  public OpenAPIModelSchemaGenerator(OpenAPI openAPI) {
    this.openAPI = openAPI;
  }

  public void generateSchemas(String basePackage) {
    ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(Schema.class));

    for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
      try {
        Class<?> modelClass = ClassUtils.forName(Objects.requireNonNull(bd.getBeanClassName()), this.getClass().getClassLoader());
        processClass(modelClass);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException("Failed to load class: " + bd.getBeanClassName(), e);
      }
    }
  }

  private void processClass(Class<?> modelClass) {
    String modelName = modelClass.getSimpleName();

    // 防止循环引用
    if (processedClasses.contains(modelClass)) {
      return;
    }
    processedClasses.add(modelClass);

    // 检查是否已存在该Schema
    if (openAPI.getComponents() != null &&
        openAPI.getComponents().getSchemas() != null &&
        openAPI.getComponents().getSchemas().containsKey(modelName)) {
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

    // 确保Components存在
    if (openAPI.getComponents() == null) {
      openAPI.setComponents(new Components());
    }

    // 添加Schema
    openAPI.getComponents().addSchemas(modelName, schema);

    // 保存Example
    if (!exampleValues.isEmpty()) {
      examples.put(modelClass, exampleValues);
    }
  }

  private void processField(Field field, Schema fieldSchema,
      Map<String, io.swagger.v3.oas.models.media.Schema> properties,
      List<String> requiredFields,
      Map<String, Object> exampleValues) {
    io.swagger.v3.oas.models.media.Schema<?> propertySchema = createPropertySchema(field);

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

  private io.swagger.v3.oas.models.media.Schema<?> createPropertySchema(Field field) {
    Type genericType = field.getGenericType();
    Class<?> type = field.getType();

    // 处理泛型类型
    if (genericType instanceof ParameterizedType parameterizedType) {
      return handleParameterizedType(parameterizedType);
    }

    // 处理基本类型
    if (type == String.class) {
      return new StringSchema();
    } else if (type == Integer.class || type == int.class) {
      return new IntegerSchema();
    } else if (type == Long.class || type == long.class) {
      return new IntegerSchema().format("int64");
    } else if (type == Double.class || type == double.class) {
      return new NumberSchema().format("double");
    } else if (type == Float.class || type == float.class) {
      return new NumberSchema().format("float");
    } else if (type == Boolean.class || type == boolean.class) {
      return new BooleanSchema();
    } else if (type == BigDecimal.class) {
      return new NumberSchema();
    } else if (type == LocalDateTime.class) {
      return new StringSchema().format("date-time");
    } else if (type.isEnum()) {
      StringSchema enumSchema = new StringSchema();
      enumSchema.setEnum(Arrays.stream(type.getEnumConstants())
          .map(Object::toString)
          .collect(Collectors.toList()));
      return enumSchema;
    }

    // 处理集合类型
    if (Collection.class.isAssignableFrom(type)) {
      return handleCollectionType(field);
    }

    // 处理Map类型
    if (Map.class.isAssignableFrom(type)) {
      return handleMapType(field);
    }

    // 处理数组
    if (type.isArray()) {
      Class<?> componentType = type.getComponentType();
      ArraySchema arraySchema = new ArraySchema();
      arraySchema.setItems(createSchemaForClass(componentType));
      return arraySchema;
    }

    // 处理复杂对象
    if (!type.isPrimitive() && !type.getPackage().getName().startsWith("java.")) {
      processClass(type); // 递归处理复杂对象
      return new io.swagger.v3.oas.models.media.Schema<>().$ref("#/components/schemas/" + type.getSimpleName());
    }

    // 默认返回Object类型
    return new ObjectSchema();
  }

  private io.swagger.v3.oas.models.media.Schema<?> handleCollectionType(Field field) {
    Type genericType = field.getGenericType();
    ArraySchema arraySchema = new ArraySchema();

    if (genericType instanceof ParameterizedType paramType) {
      Type[] typeArguments = paramType.getActualTypeArguments();
      if (typeArguments.length > 0) {
        Type actualTypeArgument = typeArguments[0];
        if (actualTypeArgument instanceof Class<?> actualClass) {
          io.swagger.v3.oas.models.media.Schema<?> itemSchema = createSchemaForClass(actualClass);
          arraySchema.setItems(itemSchema);
        }
      }
    }

    return arraySchema;
  }

  private io.swagger.v3.oas.models.media.Schema<?> handleMapType(Field field) {
    Type genericType = field.getGenericType();
    MapSchema mapSchema = new MapSchema();

    if (genericType instanceof ParameterizedType paramType) {
      Type[] typeArguments = paramType.getActualTypeArguments();
      if (typeArguments.length > 1 && typeArguments[1] instanceof Class<?> valueClass) {
        io.swagger.v3.oas.models.media.Schema<?> valueSchema = createSchemaForClass(valueClass);
        mapSchema.setAdditionalProperties(valueSchema);
      }
    }

    return mapSchema;
  }

  private io.swagger.v3.oas.models.media.Schema<?> createSchemaForClass(Class<?> clazz) {
    if (clazz.isPrimitive() || clazz == String.class || Number.class.isAssignableFrom(clazz)) {
      // 对于基本类型，直接创建对应的Schema
      if (clazz == String.class) {
        return new StringSchema();
      } else if (clazz == Integer.class || clazz == int.class) {
        return new IntegerSchema();
      } else if (clazz == Long.class || clazz == long.class) {
        return new IntegerSchema().format("int64");
      } else if (clazz == Double.class || clazz == double.class) {
        return new NumberSchema().format("double");
      } else if (clazz == Float.class || clazz == float.class) {
        return new NumberSchema().format("float");
      } else if (clazz == Boolean.class || clazz == boolean.class) {
        return new BooleanSchema();
      } else if (Number.class.isAssignableFrom(clazz)) {
        return new NumberSchema();
      }
    }

    // 对于复杂对象，先处理后返回引用
    if (!clazz.isPrimitive() && !clazz.getPackage().getName().startsWith("java.")) {
      processClass(clazz);
      return new io.swagger.v3.oas.models.media.Schema<>().$ref("#/components/schemas/" + clazz.getSimpleName());
    }

    // 默认返回Object类型
    return new ObjectSchema();
  }

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
      // 转换失败时返回原始字符串
      return example;
    }
    return example;
  }

  private io.swagger.v3.oas.models.media.Schema<?> handleParameterizedType(ParameterizedType parameterizedType) {
    Class<?> rawType = (Class<?>) parameterizedType.getRawType();
    Type[] typeArguments = parameterizedType.getActualTypeArguments();

    // 处理Collection类型
    if (Collection.class.isAssignableFrom(rawType)) {
      return createArraySchema(typeArguments[0]);
    }

    // 处理Map类型
    if (Map.class.isAssignableFrom(rawType)) {
      return createMapSchema(typeArguments);
    }

    // 处理其他泛型类型
    return createGenericTypeSchema(rawType, typeArguments);
  }

  private io.swagger.v3.oas.models.media.Schema<?> createArraySchema(Type elementType) {
    ArraySchema arraySchema = new ArraySchema();
    arraySchema.setItems(createSchemaFromType(elementType));
    return arraySchema;
  }

  private io.swagger.v3.oas.models.media.Schema<?> createMapSchema(Type[] typeArguments) {
    MapSchema mapSchema = new MapSchema();
    // 处理Map的值类型
    if (typeArguments.length > 1) {
      mapSchema.setAdditionalProperties(createSchemaFromType(typeArguments[1]));
    }
    return mapSchema;
  }

  private io.swagger.v3.oas.models.media.Schema<?> createGenericTypeSchema(Class<?> rawType, Type[] typeArguments) {
    // 处理自定义泛型类型
    processClass(rawType);
    ObjectSchema schema = new ObjectSchema();
    schema.setProperties(new LinkedHashMap<>());

    // 获取泛型类的所有字段
    for (Field field : rawType.getDeclaredFields()) {
      Schema fieldSchema = field.getAnnotation(Schema.class);
      if (fieldSchema != null) {
        Type resolvedType = resolveGenericType(field.getGenericType(), typeArguments);
        io.swagger.v3.oas.models.media.Schema<?> propertySchema = createSchemaFromType(resolvedType);
        schema.getProperties().put(field.getName(), propertySchema);
      }
    }

    return schema;
  }

  private io.swagger.v3.oas.models.media.Schema<?> createSchemaFromType(Type type) {
    if (type instanceof Class<?>) {
      return createSchemaForClass((Class<?>) type);
    } else if (type instanceof ParameterizedType) {
      return handleParameterizedType((ParameterizedType) type);
    } else if (type instanceof WildcardType wildcardType) {
      // 处理通配符类型（如 ? extends Number）
      Type[] upperBounds = wildcardType.getUpperBounds();
      if (upperBounds.length > 0) {
        return createSchemaFromType(upperBounds[0]);
      }
    } else if (type instanceof TypeVariable<?> typeVar) {
      // 处理类型变量（如 T, E 等）
      Type[] bounds = typeVar.getBounds();
      if (bounds.length > 0) {
        return createSchemaFromType(bounds[0]);
      }
    }

    return new ObjectSchema();
  }

  private Type resolveGenericType(Type type, Type[] typeArguments) {
    if (type instanceof TypeVariable<?> typeVar) {
      // 查找类型变量在声明中的位置
      TypeVariable<?>[] typeParameters = ((Class<?>) typeVar.getGenericDeclaration()).getTypeParameters();
      for (int i = 0; i < typeParameters.length; i++) {
        if (typeParameters[i].getName().equals(typeVar.getName()) && i < typeArguments.length) {
          return typeArguments[i];
        }
      }
    }
    return type;
  }

  public Map<Class<?>, Object> getExamples() {
    return examples;
  }
}
