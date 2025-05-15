package org.example.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class OpenAPIModelSchemaGenerator {

  private final OpenAPI openAPI;
  private final Map<String, Object> examples = new HashMap<>();

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
    Map<String, Object> exampleValues = new LinkedHashMap<>();

    for (Field field : modelClass.getDeclaredFields()) {
      Schema fieldSchema = field.getAnnotation(Schema.class);
      if (fieldSchema != null) {
        processField(field, fieldSchema, properties, exampleValues);
      }
    }

    schema.setProperties(properties);

    // 确保Components存在
    if (openAPI.getComponents() == null) {
      openAPI.setComponents(new Components());
    }

    // 添加Schema
    openAPI.getComponents().addSchemas(modelName, schema);

    // 保存Example
    if (!exampleValues.isEmpty()) {
      examples.put(modelName, exampleValues);
    }
  }

  private void processField(Field field, Schema fieldSchema,
      Map<String, io.swagger.v3.oas.models.media.Schema> properties,
      Map<String, Object> exampleValues) {
    io.swagger.v3.oas.models.media.Schema<?> propertySchema = createPropertySchema(field);

    if (StringUtils.hasText(fieldSchema.description())) {
      propertySchema.description(fieldSchema.description());
    }

    if (fieldSchema.requiredMode() == Schema.RequiredMode.REQUIRED) {
      propertySchema.addRequiredItem(fieldSchema.name());
    }

    if (StringUtils.hasText(fieldSchema.example())) {
      Object example = convertExample(fieldSchema.example(), field.getType());
      propertySchema.example(example);
      exampleValues.put(field.getName(), example);
    }

    properties.put(field.getName(), propertySchema);
  }

  private io.swagger.v3.oas.models.media.Schema<?> createPropertySchema(Field field) {
    Class<?> type = field.getType();

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
      // TODO
    }

    // 对于复杂对象，创建引用
    return new io.swagger.v3.oas.models.media.Schema<>().$ref("#/components/schemas/" + type.getSimpleName());
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

  public Map<String, Object> getExamples() {
    return examples;
  }
}
