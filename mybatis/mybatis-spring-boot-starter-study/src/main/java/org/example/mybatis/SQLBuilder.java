package org.example.mybatis;

import java.util.List;
import org.apache.ibatis.jdbc.SQL;
import org.example.datasource.DataSourceContextHolder;
import org.example.datasource.DataSourceType;

// SQL构建器类
public class SQLBuilder {

  private final SQL sql;
  private final String dataSourceType;

  public SQLBuilder() {
    this.sql = new SQL();
    this.dataSourceType = DataSourceContextHolder.getDataSourceType();
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

  public String build() {
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