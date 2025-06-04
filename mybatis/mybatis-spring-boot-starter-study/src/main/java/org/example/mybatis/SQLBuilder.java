package org.example.mybatis;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.jdbc.SQL;
import org.example.datasource.DatabaseType;
import org.example.mybatis.exception.InvalidSqlException;
import org.example.mybatis.model.FieldValue;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.sort.SortableItem;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * SQL Builder Class
 */
@Slf4j
public class SQLBuilder {

  private static final String PRIMARY_KEY = "id";
  private static final String COUNT_COLUMN = "COUNT(1)";
  private static final String ALL_COLUMNS = "*";

  private final SQL sql;
  private String sqlAsString;

  private SQLBuilder() {
    this(true);
  }

  private SQLBuilder(boolean shouldInitializeSql) {
    this.sql = shouldInitializeSql ? new SQL() : null;
  }

  public static SQLBuilder builder() {
    return new SQLBuilder();
  }

  public static SQLBuilder builder(boolean shouldInitializeSql) {
    return new SQLBuilder(shouldInitializeSql);
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
    sql.SELECT(COUNT_COLUMN).FROM(tableName);
    return this;
  }

  public SQLBuilder buildSelectQueryWithAllColumns(String tableName) {
    sql.SELECT(ALL_COLUMNS).FROM(tableName);
    return this;
  }

  public SQLBuilder buildSelectQueryWithColumns(String tableName, List<String> columns) {
    sql.SELECT(formatSelectColumns(columns)).FROM(tableName);
    return this;
  }

  public SQLBuilder buildWhereClauseWithConditions(List<Condition> conditions) {
    addConditions(conditions);
    return this;
  }

  public SQLBuilder buildWhereClauseWithFields(List<FieldValue> fieldValues) {
    if (!CollectionUtils.isEmpty(fieldValues)) {
      fieldValues.forEach(field -> sql.WHERE(formatAssignment(field.columnName(), field.fieldName())));
    }
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

  public SQLBuilder buildBatchInsertQuery(String tableName, List<Field> fields, List<Object> domains) {
    final DatabaseType databaseType = SQLHelper.getDatabaseType();
    sqlAsString = switch (databaseType) {
      case DatabaseType.MYSQL, DatabaseType.POSTGRESQL -> buildDefaultBatchInsertQuery(tableName, fields, domains);
      case DatabaseType.ORACLE -> buildOracleBatchInsertQuery(tableName, fields, domains);
    };
    return this;
  }

  public String build() {
    String sqlToUse = null;
    if (Objects.nonNull(sql)) {
      sqlToUse = wrapSqlWithScriptTag(sql.toString());
    } else if (StringUtils.hasText(sqlAsString)) {
      sqlToUse = wrapSqlWithScriptTag(sqlAsString);
    }
    if (!StringUtils.hasText(sqlToUse)) {
      throw new InvalidSqlException();
    }
    debugSql(sqlToUse);
    return sqlToUse;
  }

  private String buildDefaultBatchInsertQuery(String tableName, List<Field> fields, List<Object> domains) {
    String columnNames = formatBatchInsertColumns(fields);
    String values = domains.stream()
        .map(domain -> formatBatchInsertValues(fields))
        .collect(Collectors.joining(", "));
    return "INSERT INTO %s (%s) VALUES %s".formatted(tableName, columnNames, values);
  }

  private String buildOracleBatchInsertQuery(String tableName, List<Field> fields, List<Object> domains) {
    StringBuilder sqlBuilder = new StringBuilder("INSERT ALL ");
    domains.forEach(domain -> sqlBuilder.append(formatOracleBatchInsertClause(tableName, fields, domain)));
    sqlBuilder.append("SELECT * FROM dual");
    return sqlBuilder.toString();
  }

  private String formatOracleBatchInsertClause(String tableName, List<Field> fields, Object domain) {
    String columnNames = formatBatchInsertColumns(fields);
    String values = formatBatchInsertValues(fields);
    return "INTO %s (%s) VALUES (%s) ".formatted(tableName, columnNames, values);
  }

  private String formatBatchInsertColumns(List<Field> fields) {
    return fields.stream()
        .map(Field::getName)
        .collect(Collectors.joining(", "));
  }

  private String formatBatchInsertValues(List<Field> fields) {
    return fields.stream()
        .map(field -> "#{domain.%s}".formatted(field.getName()))
        .collect(Collectors.joining(", "));
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
            .map(column -> SQLHelper.formatName(SQLHelper.camelCaseToUpperSnakeCase(column)))
            .collect(Collectors.joining(", "));
  }

  private String formatOrderBy(List<SortableItem> sorting) {
    return sorting.stream()
        .map(item -> "%s %s".formatted(SQLHelper.formatName(SQLHelper.camelCaseToUpperSnakeCase(item.getField())),
            formatSortDirection(item.getSort())))
        .collect(Collectors.joining(", "));
  }

  private String formatSortDirection(String sort) {
    return "DESC".equalsIgnoreCase(sort) ? "DESC" : "ASC";
  }

  private boolean isPrimaryKey(FieldValue field) {
    return PRIMARY_KEY.equals(field.fieldName());
  }

  private void addPrimaryKeyCondition() {
    sql.WHERE(formatAssignment(PRIMARY_KEY, PRIMARY_KEY));
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

  private String wrapSqlWithScriptTag(String sqlToUse) {
    return "<script>" + sqlToUse + "</script>";
  }

  private void debugSql(String sqlToUse) {
    if (log.isDebugEnabled()) {
      log.debug("==>  Preparing: {}", SqlSourceBuilder.removeExtraWhitespaces(sqlToUse));
    }
  }
}