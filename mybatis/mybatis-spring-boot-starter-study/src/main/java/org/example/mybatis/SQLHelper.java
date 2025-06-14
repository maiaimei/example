package org.example.mybatis;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
import org.example.mybatis.annotation.TableColumn;
import org.example.mybatis.annotation.TableName;
import org.example.mybatis.constant.DatabaseType;
import org.example.mybatis.model.FieldMetadata;
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
  public static void validateDomainField(List<FieldMetadata> fieldMetadataList) {
    if (CollectionUtils.isEmpty(fieldMetadataList)) {
      throw new IllegalArgumentException("Domain fields cannot be null or empty");
    }
  }

  // 验证域对象列表是否为空
  public static void validateDomains(List<?> domains) {
    if (CollectionUtils.isEmpty(domains)) {
      throw new IllegalArgumentException("Domains cannot be null or empty");
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
    TableName tableNameAnnotation = findTableNameAnnotation(clazz);
    String tableName = Objects.nonNull(tableNameAnnotation) ? tableNameAnnotation.value() : clazz.getSimpleName();
    return formatName(camelCaseToUpperSnakeCase(tableName));
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
   * 解析类的所有字段（包括父类字段），并进行处理：
   * <p> 1. 排除静态字段、瞬态字段等。
   * <p> 2. 去重（子类覆盖父类字段）。
   * <p> 3. 排序（确保字段顺序一致）。
   */
  public static List<Field> resolveFields(Class<?> clazz) {
    Map<String, Field> fieldMap = new LinkedHashMap<>();

    while (Objects.nonNull(clazz) && clazz != Object.class) {
      Arrays.stream(clazz.getDeclaredFields())
          .filter(field -> {
            int modifiers = field.getModifiers();
            // 排除静态字段、瞬态字段等
            if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
              return false;
            }
            // 检查是否有@Transient注解
            return !field.isAnnotationPresent(Transient.class);
          })
          .forEach(field -> fieldMap.putIfAbsent(field.getName(), field));
      clazz = clazz.getSuperclass();
    }

    // 排序字段并返回
    return fieldMap.values().stream()
        .sorted(Comparator.comparing(Field::getName))
        .toList();
  }

  // 获取非空字段值
  public static List<FieldMetadata> getNotNullFieldMetadataList(Object domain) {
    validateDomain(domain);
    List<FieldMetadata> fieldMetadata = new ArrayList<>();
    for (Field field : getFields(domain.getClass())) {
      field.setAccessible(true);
      try {
        Object value = field.get(domain);
        if (Objects.nonNull(value)) {
          fieldMetadata.add(createFieldMetadata(field, value));
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Failed to get field value", e);
      }
    }
    return fieldMetadata;
  }

  // 创建字段值对象
  private static FieldMetadata createFieldMetadata(Field field, Object value) {
    TableColumn columnAnnotation = field.getAnnotation(TableColumn.class);
    String columnName;
    String columnType;
    if (Objects.nonNull(columnAnnotation)) {
      columnName = formatColumnName(columnAnnotation.value());
      columnType = columnAnnotation.type();
    } else {
      columnName = formatColumnName(field.getName());
      columnType = null;
    }
    return new FieldMetadata(field.getName(), columnName, columnType, value);
  }

  // 格式化列名
  public static String formatColumnName(String name) {
    return formatName(camelCaseToUpperSnakeCase(name));
  }

  // 格式化表名或列名
  public static String formatName(String name) {
    final String databaseType = getDatabaseTypeAsString();
    return DatabaseType.POSTGRESQL.getType().equals(databaseType) ? "\"" + name + "\"" : name;
  }

  // 驼峰命名转大蛇形命名
  public static String camelCaseToUpperSnakeCase(String camelCase) {
    return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase(Locale.US);
  }

  public static DatabaseType getDatabaseType() {
//    return Optional.ofNullable(getDatabaseTypeAsString())
//        .map(String::toUpperCase)
//        .map(DatabaseType::valueOf)
//        .orElse(DatabaseType.MYSQL);
    return DatabaseType.MYSQL;
  }

  // 获取数据库类型
  public static String getDatabaseTypeAsString() {
//    return Optional.ofNullable(DataSourceContextHolder.getDataSourceType())
//        .orElse(Optional.ofNullable(System.getProperty("app.dbType")).orElse(DatabaseType.MYSQL.getType()));
    return DatabaseType.MYSQL.getType();
  }

}
