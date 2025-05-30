package org.example.mybatis;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.example.mybatis.annotation.TableName;
import org.springframework.util.CollectionUtils;

public class SQLHelper {

  // 类字段缓存
  private static final Map<Class<?>, List<Field>> CLASS_FIELDS_CACHE = new ConcurrentHashMap<>();
  // 表名缓存
  private static final Map<Class<?>, String> TABLE_NAME_CACHE = new ConcurrentHashMap<>();

  public static void validateDomain(Object domain) {
    if (Objects.isNull(domain)) {
      throw new IllegalArgumentException("Domain object cannot be null");
    }
  }

  public static void validateDomainField(List<FieldValue> fieldValueList) {
    if (CollectionUtils.isEmpty(fieldValueList)) {
      throw new IllegalArgumentException("Domain fields cannot be null or empty");
    }
  }

  /**
   * 获取表名
   * <p> 1. 优先从缓存获取
   * <p> 2. 查找类及其父类上的@TableName注解
   * <p> 3. 如果没有注解，则使用类名转换
   */
  public static String getTableName(Class<?> clazz) {
    return TABLE_NAME_CACHE.computeIfAbsent(clazz, SQLHelper::resolveTableName);
  }

  /**
   * 解析表名
   * <p> 1. 先查找当前类的@TableName注解
   * <p> 2. 如果没有，递归查找父类的@TableName注解
   * <p> 3. 如果都没有注解，则将类名转换为下划线格式
   */
  private static String resolveTableName(Class<?> clazz) {
    // 查找当前类及其父类的@TableName注解
    TableName tableName = findTableNameAnnotation(clazz);
    if (Objects.nonNull(tableName)) {
      return tableName.value().toUpperCase(Locale.US);
    }

    // 如果没有注解，则将类名转换为下划线格式
    return camelToUnderscore(clazz.getSimpleName());
  }

  /**
   * 递归查找类及其父类上的@TableName注解
   */
  private static TableName findTableNameAnnotation(Class<?> clazz) {
    if (Objects.isNull(clazz) || clazz == Object.class) {
      return null;
    }

    // 查找当前类的注解
    TableName tableName = clazz.getAnnotation(TableName.class);
    if (Objects.nonNull(tableName)) {
      return tableName;
    }

    // 递归查找父类的注解
    return findTableNameAnnotation(clazz.getSuperclass());
  }

  /**
   * 获取类及其父类的所有字段 1. 优先从缓存获取 2. 递归获取类及其父类的所有字段 3. 过滤和排序字段
   */
  public static List<Field> getFields(Class<?> clazz) {
    return CLASS_FIELDS_CACHE.computeIfAbsent(clazz, SQLHelper::resolveFields);
  }

  /**
   * 解析类的所有字段（包括父类字段）
   */
  private static List<Field> resolveFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    Class<?> currentClass = clazz;

    // 递归获取类及其父类的所有字段
    while (Objects.nonNull(currentClass) && currentClass != Object.class) {
      // 获取当前类声明的所有字段
      Field[] declaredFields = currentClass.getDeclaredFields();
      // 将字段添加到列表中
      fields.addAll(Arrays.asList(declaredFields));
      // 获取父类
      currentClass = currentClass.getSuperclass();
    }

    // 对字段进行处理和排序
    return processFields(fields);
  }

  /**
   * 处理字段列表
   * <p> 1. 去重（如果子类覆盖了父类的字段）
   * <p> 2. 排序（确保字段顺序一致）
   */
  private static List<Field> processFields(List<Field> fields) {
    // 使用LinkedHashMap保持顺序，同时去重
    Map<String, Field> fieldMap = new LinkedHashMap<>();

    // 按照字段名去重，子类的字段优先
    fields.forEach(field -> {
      if (!fieldMap.containsKey(field.getName())) {
        fieldMap.put(field.getName(), field);
      }
    });

    // 将字段列表排序（可选，根据需求决定是否需要排序）
    return fieldMap.values().stream()
        .sorted(Comparator.comparing(Field::getName))
        .collect(Collectors.toList());
  }

  // 获取非空字段值
  public static List<FieldValue> getNotNullFieldValues(Object domain) {
    List<FieldValue> fieldValues = new ArrayList<>();
    List<Field> fields = getFields(domain.getClass());

    for (Field field : fields) {
      field.setAccessible(true);
      try {
        Object value = field.get(domain);
        if (Objects.nonNull(value)) {
          fieldValues.add(new FieldValue(
              field.getName(),
              camelToUnderscore(field.getName()),
              value));
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Failed to get field value", e);
      }
    }
    return fieldValues;
  }

  // 驼峰转下划线
  public static String camelToUnderscore(String camelCase) {
    return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase(Locale.US);
  }
}
