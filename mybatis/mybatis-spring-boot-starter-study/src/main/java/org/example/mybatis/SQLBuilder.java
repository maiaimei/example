package org.example.mybatis;

import java.util.List;
import org.apache.ibatis.jdbc.SQL;

// SQL构建器类
public class SQLBuilder {

  private final SQL sql;

  public SQLBuilder() {
    this.sql = new SQL();
  }

  public static SQLBuilder builder() {
    return new SQLBuilder();
  }

  public SQLBuilder insert(String tableName, List<FieldValue> fieldValues) {
    sql.INSERT_INTO(tableName);
    fieldValues.forEach(field ->
        sql.VALUES(field.columnName(), "#{%s}".formatted(field.fieldName())));
    return this;
  }

  public SQLBuilder update(String tableName, List<FieldValue> fieldValues) {
    sql.UPDATE(tableName);
    fieldValues.stream()
        .filter(field -> !field.fieldName().equals("id"))
        .forEach(field ->
            sql.SET("%s = #{%s}".formatted(field.columnName(), field.fieldName())));
    sql.WHERE("id = #{id}");
    return this;
  }

  public SQLBuilder delete(String tableName) {
    sql.DELETE_FROM(tableName).WHERE("id = #{id}");
    return this;
  }

  public SQLBuilder select(String tableName) {
    sql.SELECT("*").FROM(tableName);
    return this;
  }

  public String build() {
    return sql.toString();
  }
}