package org.example.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.Getter;
import org.apache.ibatis.jdbc.SQL;
import org.example.datasource.DataSourceContextHolder;
import org.example.datasource.DataSourceType;
import org.example.mybatis.model.FieldValue;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.sort.SortableItem;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

// SQL构建器类
public class SQLBuilder {

  private final SQL sql;
  private final String dataSourceType;

  @Getter
  private final Map<String, Object> parameters = new HashMap<>();
  private int parameterIndex = 0;

  private SQLBuilder() {
    this.sql = new SQL();
    this.dataSourceType = Optional.ofNullable(DataSourceContextHolder.getDataSourceType())
        .orElse(DataSourceType.MYSQL.getType());
  }

  public static SQLBuilder builder() {
    return new SQLBuilder();
  }

  public SQLBuilder insert(String tableName, List<FieldValue> fieldValues) {
    sql.INSERT_INTO(formatName(tableName));
    fieldValues.forEach(field -> sql.VALUES(formatName(field.columnName()), formatParameter(field.fieldName())));
    return this;
  }

  public SQLBuilder update(String tableName, List<FieldValue> fieldValues) {
    sql.UPDATE(formatName(tableName));
    fieldValues.stream()
        .filter(field -> !isPrimaryKey(field))
        .forEach(field -> sql.SET(formatAssignment(field.columnName(), field.fieldName())));
    sql.WHERE(formatAssignment("id", "id"));
    return this;
  }

  public SQLBuilder delete(String tableName) {
    sql.DELETE_FROM(formatName(tableName)).WHERE(formatAssignment("id", "id"));
    return this;
  }

  public SQLBuilder selectAllColumns(String tableName) {
    sql.SELECT("*").FROM(formatName(tableName));
    return this;
  }

  public SQLBuilder selectColumns(String tableName, List<String> columns) {
    sql.SELECT(formatSelectColumns(columns)).FROM(formatName(tableName));
    return this;
  }

  public SQLBuilder whereByFieldValues(List<FieldValue> fieldValues) {
    if (!CollectionUtils.isEmpty(fieldValues)) {
      fieldValues.forEach(field -> sql.WHERE(formatAssignment(field.columnName(), field.fieldName())));
    }
    return this;
  }

  public SQLBuilder whereByConditions(List<Condition> conditions, AtomicInteger index) {
    if (!CollectionUtils.isEmpty(conditions)) {
      conditions.forEach(condition -> {
        String whereSql = condition.build(dataSourceType, index);
        if (StringUtils.hasText(whereSql)) {
          sql.WHERE(whereSql);
        }
      });
    }
    return this;
  }

  public SQLBuilder orderBy(List<SortableItem> sorting) {
    if (!CollectionUtils.isEmpty(sorting)) {
      sql.ORDER_BY(formatOrderBy(sorting));
    }
    return this;
  }

  public String build() {
    return sql.toString();
  }

  private String formatName(String name) {
    return SQLHelper.formatName(dataSourceType, name);
  }

  private String formatParameter(String fieldName) {
    return "#{%s}".formatted(fieldName);
  }

  private String formatAssignment(String columnName, String fieldName) {
    return "%s = %s".formatted(formatName(columnName), formatParameter(fieldName));
  }

  private String formatSelectColumns(List<String> columns) {
    return CollectionUtils.isEmpty(columns) ? "*" : String.join(", ", columns.stream().map(this::formatName).toList());
  }

  private String formatOrderBy(List<SortableItem> sorting) {
    return sorting.stream()
        .map(item -> "%s %s".formatted(formatName(item.getField()), formatSortDirection(item.getSort())))
        .collect(Collectors.joining(", "));
  }

  private String formatSortDirection(String sort) {
    return "DESC".equalsIgnoreCase(sort) ? "DESC" : "ASC";
  }

  private boolean isPrimaryKey(FieldValue field) {
    return "id".equals(field.fieldName());
  }
}