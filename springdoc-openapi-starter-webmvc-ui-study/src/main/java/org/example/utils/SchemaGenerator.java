package org.example.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SchemaGenerator {

  public static Schema<?> generateSchema(Class<?> modelClass) {
    return generateSchema(modelClass, new HashSet<>());
  }

  private static Schema<?> generateSchema(Class<?> modelClass, Set<Class<?>> processedClasses) {
    // 防止循环引用
    if (processedClasses.contains(modelClass)) {
      return new Schema<>().$ref("#/components/schemas/" + modelClass.getSimpleName());
    }
    processedClasses.add(modelClass);

    // 处理基本类型
    Schema<?> basicSchema = generateBasicTypeSchema(modelClass);
    if (basicSchema != null) {
      return basicSchema;
    }

    // 处理集合类型
    if (Collection.class.isAssignableFrom(modelClass)) {
      return new ArraySchema();
    }

    // 处理对象类型
    Schema<?> schema = new Schema<>()
        .type("object")
        .description(getClassDescription(modelClass));

    // 处理类的所有字段
    for (Field field : modelClass.getDeclaredFields()) {
      processField(field, schema, processedClasses);
    }

    return schema;
  }

  private static void processField(Field field, Schema<?> schema, Set<Class<?>> processedClasses) {
    // 忽略静态和瞬态字段
    if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) ||
        java.lang.reflect.Modifier.isTransient(field.getModifiers())) {
      return;
    }

    String fieldName = getFieldName(field);
    Schema<?> fieldSchema = generateFieldSchema(field, processedClasses);

    if (fieldSchema != null) {
      schema.addProperty(fieldName, fieldSchema);

      // 处理必填字段
      if (isRequired(field)) {
        schema.addRequiredItem(fieldName);
      }
    }
  }

  private static Schema<?> generateFieldSchema(Field field, Set<Class<?>> processedClasses) {
    Class<?> fieldType = field.getType();
    Schema<?> fieldSchema;

    // 处理集合类型
    if (Collection.class.isAssignableFrom(fieldType)) {
      fieldSchema = new ArraySchema();
      Class<?> genericType = getGenericType(field);
      if (genericType != null) {
        ((ArraySchema) fieldSchema).setItems(generateSchema(genericType, processedClasses));
      }
    }
    // 处理Map类型
    else if (Map.class.isAssignableFrom(fieldType)) {
      fieldSchema = new MapSchema();
      Class<?> valueType = getMapValueType(field);
      if (valueType != null) {
        ((MapSchema) fieldSchema).additionalProperties(generateSchema(valueType, processedClasses));
      }
    }
    // 处理基本类型和对象类型
    else {
      fieldSchema = generateSchema(fieldType, processedClasses);
    }

    // 处理字段注解
    applyFieldAnnotations(field, fieldSchema);

    return fieldSchema;
  }

  private static Schema<?> generateBasicTypeSchema(Class<?> type) {
    // 字符串类型
    if (String.class.equals(type)) {
      return new StringSchema();
    }
    // 数值类型
    if (Integer.class.equals(type) || int.class.equals(type)) {
      return new IntegerSchema().format("int32");
    }
    if (Long.class.equals(type) || long.class.equals(type)) {
      return new IntegerSchema().format("int64");
    }
    if (Float.class.equals(type) || float.class.equals(type)) {
      return new NumberSchema().format("float");
    }
    if (Double.class.equals(type) || double.class.equals(type)) {
      return new NumberSchema().format("double");
    }
    if (BigDecimal.class.equals(type)) {
      return new NumberSchema();
    }
    // 布尔类型
    if (Boolean.class.equals(type) || boolean.class.equals(type)) {
      return new BooleanSchema();
    }
    // 日期时间类型
    if (Date.class.equals(type)) {
      return new DateTimeSchema();
    }
    if (LocalDateTime.class.equals(type)) {
      return new DateTimeSchema();
    }
    if (LocalDate.class.equals(type)) {
      return new DateSchema();
    }

    return null;
  }

  private static void applyFieldAnnotations(Field field, Schema<?> schema) {
    // 处理@NotNull注解
    if (field.isAnnotationPresent(NotNull.class)) {
      schema.setNullable(false);
    }

    // 处理@Size注解
    Size sizeAnnotation = field.getAnnotation(Size.class);
    if (sizeAnnotation != null) {
      if (schema instanceof StringSchema) {
        schema.setMinLength(sizeAnnotation.min());
        schema.setMaxLength(sizeAnnotation.max());
      } else if (schema instanceof ArraySchema) {
        schema.setMinItems(sizeAnnotation.min());
        schema.setMaxItems(sizeAnnotation.max());
      }
    }

    // 处理@Min和@Max注解
    Min minAnnotation = field.getAnnotation(Min.class);
    if (minAnnotation != null) {
      schema.setMinimum(BigDecimal.valueOf(minAnnotation.value()));
    }

    Max maxAnnotation = field.getAnnotation(Max.class);
    if (maxAnnotation != null) {
      schema.setMaximum(BigDecimal.valueOf(maxAnnotation.value()));
    }

    // 处理@Pattern注解
    Pattern patternAnnotation = field.getAnnotation(Pattern.class);
    if (patternAnnotation != null && schema instanceof StringSchema) {
      schema.setPattern(patternAnnotation.regexp());
    }

    // 处理@Email注解
    if (field.isAnnotationPresent(Email.class) && schema instanceof StringSchema) {
      schema.setFormat("email");
    }

    // 处理描述注解
    schema.setDescription(getFieldDescription(field));

    // 要扩展支持更多注解或类型，只需在相应的方法中添加处理逻辑即可。例如，添加新的注解支持：
//    MyCustomAnnotation customAnnotation = field.getAnnotation(MyCustomAnnotation.class);
//    if (customAnnotation != null) {
//      // 处理自定义注解
//      schema.addExtension("x-custom", customAnnotation.value());
//    }
  }

  private static String getFieldName(Field field) {
    JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
    return jsonProperty != null ? jsonProperty.value() : field.getName();
  }

  private static boolean isRequired(Field field) {
    return field.isAnnotationPresent(NotNull.class);
  }

  private static Class<?> getGenericType(Field field) {
    if (field.getGenericType() instanceof ParameterizedType) {
      ParameterizedType paramType = (ParameterizedType) field.getGenericType();
      return (Class<?>) paramType.getActualTypeArguments()[0];
    }
    return null;
  }

  private static Class<?> getMapValueType(Field field) {
    if (field.getGenericType() instanceof ParameterizedType) {
      ParameterizedType paramType = (ParameterizedType) field.getGenericType();
      return (Class<?>) paramType.getActualTypeArguments()[1];
    }
    return null;
  }

  private static String getClassDescription(Class<?> clazz) {
    // 这里可以处理类级别的描述注解
    return null;
  }

  private static String getFieldDescription(Field field) {
    // 这里可以处理字段级别的描述注解
    return null;
  }
}
