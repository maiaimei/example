package org.example.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.apache.ibatis.jdbc.SQL;
import org.example.datasource.DataSourceContextHolder;
import org.example.datasource.DataSourceType;

// SQL构建器类
public class SQLBuilder {

  private final SQL sql;
  private final String dataSourceType;
  private final List<Consumer<SQL>> whereConditions;

  public SQLBuilder() {
    this.sql = new SQL();
    this.dataSourceType = DataSourceContextHolder.getDataSourceType();
    this.whereConditions = new ArrayList<>();
  }

  public static SQLBuilder builder() {
    return new SQLBuilder();
  }

  public SQLBuilder insert(String tableName, List<FieldValue> fieldValues) {
    sql.INSERT_INTO(formattedName(tableName));
    fieldValues.forEach(field ->
        sql.VALUES(formattedName(field.columnName()), "#{%s}".formatted(field.fieldName())));
    return this;
  }

  public SQLBuilder update(String tableName, List<FieldValue> fieldValues) {
    sql.UPDATE(formattedName(tableName));
    fieldValues.stream()
        .filter(field -> !field.fieldName().equals("id"))
        .forEach(field ->
            sql.SET("%s = #{%s}".formatted(formattedName(field.columnName()), field.fieldName())));
    sql.WHERE("id = #{id}");
    return this;
  }

  public SQLBuilder delete(String tableName) {
    sql.DELETE_FROM(formattedName(tableName)).WHERE("id = #{id}");
    return this;
  }

  public SQLBuilder select(String tableName, List<FieldValue> fieldValues) {
    sql.SELECT("*").FROM(formattedName(tableName));
    fieldValues.forEach(field ->
        sql.WHERE("%s = #{%s}".formatted(formattedName(field.columnName()), field.fieldName())));
    return this;
  }

  // 支持指定列的select方法
  public SQLBuilder select(String tableName, String... columns) {
    String selectColumns = columns.length > 0
        ? String.join(", ", columns)
        : "*";
    sql.SELECT(selectColumns).FROM(formattedName(tableName));
    return this;
  }

  // 添加等于条件
  public SQLBuilder whereEqual(String column, String fieldName) {
    whereConditions.add(sql ->
        sql.WHERE(String.format("%s = #{%s}", formattedName(column), fieldName)));
    return this;
  }

  // 添加大于条件
  public SQLBuilder whereGreaterThan(String column, String fieldName) {
    whereConditions.add(sql ->
        sql.WHERE(String.format("%s > #{%s}", formattedName(column), fieldName)));
    return this;
  }

  // 添加小于条件
  public SQLBuilder whereLessThan(String column, String fieldName) {
    whereConditions.add(sql ->
        sql.WHERE(String.format("%s < #{%s}", formattedName(column), fieldName)));
    return this;
  }

  // 添加LIKE条件
  public SQLBuilder whereLike(String column, String fieldName) {
    whereConditions.add(sql ->
        sql.WHERE(String.format("%s LIKE #{%s}", formattedName(column), fieldName)));
    return this;
  }

  // 添加IN条件
  public SQLBuilder whereIn(String column, String fieldName) {
    whereConditions.add(sql ->
        sql.WHERE(String.format("%s IN #{%s}", formattedName(column), fieldName)));
    return this;
  }

  // 添加自定义条件
  public SQLBuilder whereCustom(String condition) {
    whereConditions.add(sql -> sql.WHERE(condition));
    return this;
  }

  // 添加批量条件的方法
  public SQLBuilder whereConditions(Consumer<SQL> conditions) {
    conditions.accept(sql);
    return this;
  }

  // 添加排序功能
  public SQLBuilder orderBy(String sortField, String sortOrder) {
    if (sortField != null && !sortField.isEmpty()) {
      String order = "DESC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC";
      sql.ORDER_BY(String.format("%s %s", formattedName(sortField), order));
    }
    return this;
  }

  public String build() {
    // 应用所有WHERE条件
    whereConditions.forEach(condition -> condition.accept(sql));
    return sql.toString();
  }

  private String formattedName(String name) {
    if (DataSourceType.POSTGRESQL.getType().equals(dataSourceType)) {
      // 保持原有大小写，使用双引号
      return "\"" + name + "\"";
    }
    return name;
  }
}