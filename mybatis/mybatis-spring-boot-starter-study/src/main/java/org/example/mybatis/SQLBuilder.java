package org.example.mybatis;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.ibatis.jdbc.SQL;
import org.example.datasource.DataSourceContextHolder;
import org.example.datasource.DataSourceType;
import org.example.mybatis.model.FieldValue;
import org.example.mybatis.query.filter.FilterableItem;
import org.example.mybatis.query.sort.SortableItem;
import org.springframework.util.CollectionUtils;

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
    sql.INSERT_INTO(formatName(tableName));
    fieldValues.forEach(field -> sql.VALUES(formatName(field.columnName()), "#{%s}".formatted(field.fieldName())));
    return this;
  }

  public SQLBuilder update(String tableName, List<FieldValue> fieldValues) {
    sql.UPDATE(formatName(tableName));
    fieldValues.stream()
        .filter(field -> !field.fieldName().equals("id"))
        .forEach(field -> sql.SET("%s = #{%s}".formatted(formatName(field.columnName()), field.fieldName())));
    sql.WHERE("id = #{id}");
    return this;
  }

  public SQLBuilder delete(String tableName) {
    sql.DELETE_FROM(formatName(tableName)).WHERE("id = #{id}");
    return this;
  }

  // 查询所有列（带条件的查询）
  public SQLBuilder selectAllColumnsWithConditions(String tableName, List<FieldValue> fieldValues) {
    sql.SELECT("*").FROM(formatName(tableName));
    fieldValues.forEach(field -> sql.WHERE("%s = #{%s}".formatted(formatName(field.columnName()), field.fieldName())));
    return this;
  }

  // 列选择查询
  public SQLBuilder selectSpecificColumns(String tableName, List<String> columns) {
    String selectColumns = !CollectionUtils.isEmpty(columns)
        ? String.join(", ", columns)
        : "*";
    sql.SELECT(selectColumns).FROM(formatName(tableName));
    return this;
  }

  public SQLBuilder where(List<FilterableItem> conditions) {
    if (!CollectionUtils.isEmpty(conditions)) {
      for (int i = 0; i < conditions.size(); i++) {
        FilterableItem condition = conditions.get(i);
        final String column = formatName(condition.getColumn());
        switch (condition.getOperator()) {
          case EQ:
            sql.WHERE(String.format("%s = #{queryable.conditions[%d].value}", column, i));
            break;
          case NE:
            sql.WHERE(String.format("%s <> #{queryable.conditions[%d].value}", column, i));
            break;
          case LIKE:
            sql.WHERE(String.format("%s LIKE CONCAT('%%', #{queryable.conditions[%d].value}, '%%')", column, i));
            break;
          case IN:
            sql.WHERE(String.format("%s IN " +
                "<foreach collection='queryable.conditions[%d].value' item='item' open='(' separator=',' close=')'>" +
                "#{item}" +
                "</foreach>", column, i));
            break;
          case BETWEEN:
            sql.WHERE(String.format(
                "%s BETWEEN #{queryable.conditions[%d].value} AND #{queryable.conditions[%d].secondValue}",
                column, i, i));
            break;
          case IS_NULL:
            sql.WHERE(String.format("%s IS NULL", column));
            break;
          case IS_NOT_NULL:
            sql.WHERE(String.format("%s IS NOT NULL", column));
            break;
          // ... 其他操作符的处理
        }
      }
    }
    return this;
  }

  // 添加排序功能
  public SQLBuilder orderBy(List<SortableItem> sorting) {
    if (!CollectionUtils.isEmpty(sorting)) {
      List<String> orderClauses = sorting.stream()
          .map(item -> String.format("%s %s",
              formatName(item.getField()),
              "DESC".equalsIgnoreCase(item.getSort()) ? "DESC" : "ASC"))
          .collect(Collectors.toList());

      sql.ORDER_BY(String.join(", ", orderClauses));
    }
    return this;
  }

  public String build() {
    return sql.toString();
  }

  private String formatName(String name) {
    if (DataSourceType.POSTGRESQL.getType().equals(dataSourceType)) {
      // 保持原有大小写，使用双引号
      return "\"" + name + "\"";
    }
    return name;
  }
}