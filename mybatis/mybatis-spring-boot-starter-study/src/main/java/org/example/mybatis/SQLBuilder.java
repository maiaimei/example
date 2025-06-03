package org.example.mybatis;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.ibatis.jdbc.SQL;
import org.example.mybatis.model.FieldValue;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.sort.SortableItem;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * SQL Builder Class
 */
public class SQLBuilder {

  private final SQL sql;

  private SQLBuilder() {
    this.sql = new SQL();
  }

  public static SQLBuilder builder() {
    return new SQLBuilder();
  }

  public SQLBuilder buildInsertQuery(String tableName, List<FieldValue> fieldValues) {
    sql.INSERT_INTO(tableName);
    fieldValues.forEach(field -> sql.VALUES(field.columnName(), formatParameter(field.fieldName())));
    return this;
  }

  public SQLBuilder buildUpdateQuery(String tableName, List<FieldValue> fieldValues) {
    sql.UPDATE(tableName);
    fieldValues.stream()
        .filter(field -> !isPrimaryKey(field))
        .forEach(field -> sql.SET(formatAssignment(field.columnName(), field.fieldName())));
    addPrimaryKeyCondition();
    return this;
  }

  public SQLBuilder buildDeleteQuery(String tableName) {
    sql.DELETE_FROM(tableName);
    return this;
  }

  public SQLBuilder buildCountQuery(String tableName) {
    sql.SELECT("COUNT(1)").FROM(tableName);
    return this;
  }

  public SQLBuilder buildSelectQueryWithAllColumns(String tableName) {
    sql.SELECT("*").FROM(tableName);
    return this;
  }

  public SQLBuilder buildSelectQueryWithColumns(String tableName, List<String> columns) {
    sql.SELECT(formatSelectColumns(columns)).FROM(tableName);
    return this;
  }

  public SQLBuilder buildWhereClauseWithFields(List<FieldValue> fieldValues) {
    if (!CollectionUtils.isEmpty(fieldValues)) {
      fieldValues.forEach(field -> sql.WHERE(formatAssignment(field.columnName(), field.fieldName())));
    }
    return this;
  }

  public SQLBuilder buildWhereClauseWithConditions(List<Condition> conditions) {
    addConditions(conditions);
    return this;
  }

  public SQLBuilder buildWhereClauseWithPrimaryKey() {
    addPrimaryKeyCondition();
    return this;
  }

  public SQLBuilder buildOrderByClause(List<SortableItem> sorting) {
    if (!CollectionUtils.isEmpty(sorting)) {
      sql.ORDER_BY(formatOrderBy(sorting));
    }
    return this;
  }

  public String build() {
    return sql.toString();
  }

  // Private helper methods
  private String formatParameter(String fieldName) {
    return "#{%s}".formatted(fieldName);
  }

  private String formatAssignment(String columnName, String fieldName) {
    return "%s = %s".formatted(columnName, formatParameter(fieldName));
  }

  private String formatSelectColumns(List<String> columns) {
    return CollectionUtils.isEmpty(columns) ? "*"
        : columns.stream()
            .map(column -> SQLHelper.formatName(SQLHelper.camelToUnderscore(column)))
            .collect(Collectors.joining(", "));
  }

  private String formatOrderBy(List<SortableItem> sorting) {
    return sorting.stream()
        .map(item -> "%s %s".formatted(SQLHelper.formatName(SQLHelper.camelToUnderscore(item.getField())),
            formatSortDirection(item.getSort())))
        .collect(Collectors.joining(", "));
  }

  private String formatSortDirection(String sort) {
    return "DESC".equalsIgnoreCase(sort) ? "DESC" : "ASC";
  }

  private boolean isPrimaryKey(FieldValue field) {
    return "id".equals(field.fieldName());
  }

  private void addPrimaryKeyCondition() {
    sql.WHERE(formatAssignment("id", "id"));
  }

  private void addConditions(List<Condition> conditions) {
    if (!CollectionUtils.isEmpty(conditions)) {
      AtomicInteger index = new AtomicInteger(0);
      conditions.forEach(condition -> {
        String whereSql = condition.build(index);
        if (StringUtils.hasText(whereSql)) {
          sql.WHERE(whereSql);
        }
      });
    }
  }
}