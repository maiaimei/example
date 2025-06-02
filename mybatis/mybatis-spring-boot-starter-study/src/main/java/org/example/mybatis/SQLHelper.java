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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.example.datasource.DataSourceType;
import org.example.mybatis.annotation.ColumnName;
import org.example.mybatis.annotation.TableName;
import org.example.mybatis.model.FieldValue;
import org.springframework.util.CollectionUtils;

public class SQLHelper {

  // 类字段缓存
  private static final Map<Class<?>, List<Field>> CLASS_FIELDS_CACHE = new ConcurrentHashMap<>();
  // 表名缓存
  private static final Map<Class<?>, String> TABLE_NAME_CACHE = new ConcurrentHashMap<>();

  // 验证域对象是否为空
  public static void validateDomain(Object domain) {
    if (Objects.isNull(domain)) {
      throw new IllegalArgumentException("Domain object cannot be null");
    }
  }

  // 验证域字段是否为空
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
    TableName tableName = findTableNameAnnotation(clazz);
    return Objects.nonNull(tableName) ? tableName.value().toUpperCase(Locale.US) : camelToUnderscore(clazz.getSimpleName());
  }

  /**
   * 递归查找类及其父类上的@TableName注解
   */
  private static TableName findTableNameAnnotation(Class<?> clazz) {
    if (Objects.isNull(clazz) || clazz == Object.class) {
      return null;
    }

    return Optional.ofNullable(clazz.getAnnotation(TableName.class))
        .orElse(findTableNameAnnotation(clazz.getSuperclass()));
  }

  /**
   * 获取类及其父类的所有字段
   * <p> 1. 优先从缓存获取
   * <p> 2. 递归获取类及其父类的所有字段
   * <p> 3. 过滤和排序字段
   */
  public static List<Field> getFields(Class<?> clazz) {
    return CLASS_FIELDS_CACHE.computeIfAbsent(clazz, SQLHelper::resolveFields);
  }

  /**
   * 解析类的所有字段（包括父类字段）
   */
  private static List<Field> resolveFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    while (Objects.nonNull(clazz) && clazz != Object.class) {
      fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
      clazz = clazz.getSuperclass();
    }
    return processFields(fields);
  }

  /**
   * 处理字段列表
   * <p> 1. 去重（如果子类覆盖了父类的字段）
   * <p> 2. 排序（确保字段顺序一致）
   */
  private static List<Field> processFields(List<Field> fields) {
    Map<String, Field> fieldMap = new LinkedHashMap<>();
    fields.forEach(field -> fieldMap.putIfAbsent(field.getName(), field));
    return fieldMap.values().stream()
        .sorted(Comparator.comparing(Field::getName))
        .collect(Collectors.toList());
  }

  // 获取非空字段值
  public static List<FieldValue> getNotNullFieldValues(Object domain) {
    validateDomain(domain);
    List<FieldValue> fieldValues = new ArrayList<>();
    for (Field field : getFields(domain.getClass())) {
      field.setAccessible(true);
      try {
        Object value = field.get(domain);
        if (Objects.nonNull(value)) {
          fieldValues.add(createFieldValue(field, value));
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Failed to get field value", e);
      }
    }
    return fieldValues;
  }

  // 创建字段值对象
  private static FieldValue createFieldValue(Field field, Object value) {
    ColumnName columnName = field.getAnnotation(ColumnName.class);
    String column = Objects.nonNull(columnName) ? columnName.value() : camelToUnderscore(field.getName());
    return new FieldValue(field.getName(), column, value);
  }

  // 驼峰转下划线
  public static String camelToUnderscore(String camelCase) {
    return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase(Locale.US);
  }

  // 格式化名称
  public static String formatName(String dataSourceType, String name) {
    return DataSourceType.POSTGRESQL.getType().equals(dataSourceType) ? "\"" + name + "\"" : name;
  }
}
