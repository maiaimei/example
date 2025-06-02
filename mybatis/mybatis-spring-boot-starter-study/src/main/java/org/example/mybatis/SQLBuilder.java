package org.example.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
  private int parameterIndex = 0;
  @Getter
  private final Map<String, Object> parameters = new HashMap<>();

  public SQLBuilder() {
    this.sql = new SQL();
    this.dataSourceType = Optional.ofNullable(DataSourceContextHolder.getDataSourceType()).orElse(DataSourceType.MYSQL.getType());
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
    if (!CollectionUtils.isEmpty(fieldValues)) {
      fieldValues.forEach(field -> sql.WHERE("%s = #{%s}".formatted(formatName(field.columnName()), field.fieldName())));
    }
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

  /**
   * 构建WHERE子句
   */
  public SQLBuilder where(List<Condition> conditions) {
    if (!CollectionUtils.isEmpty(conditions)) {
      conditions.forEach(condition -> {
        String whereSql = condition.build(dataSourceType, parameterIndex);
        if (StringUtils.hasText(whereSql)) {
          sql.WHERE(whereSql);
          parameters.putAll(condition.getParameters(parameterIndex));
          parameterIndex++;
        }
      });
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
    return SQLHelper.formatName(dataSourceType, name);
  }
}