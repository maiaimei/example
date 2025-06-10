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
import org.example.mybatis.model.FieldMetadata;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.page.Pageable;
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
  private String paginationClause;

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

  public SQLBuilder buildInsertQuery(String tableName, List<FieldMetadata> fieldMetadataList) {
    sql.INSERT_INTO(tableName);
    fieldMetadataList.forEach(fieldMetadata -> sql.VALUES(fieldMetadata.getColumnName(), formatParameter(fieldMetadata)));
    return this;
  }

  public SQLBuilder buildUpdateQuery(String tableName, List<FieldMetadata> fieldMetadataList) {
    sql.UPDATE(tableName);
    fieldMetadataList.stream()
        .filter(fieldMetadata -> !isPrimaryKey(fieldMetadata))
        .forEach(fieldMetadata -> sql.SET(formatAssignment(fieldMetadata)));
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

  public SQLBuilder buildSelectQueryWithSpecialColumns(String tableName, List<String> columns) {
    sql.SELECT(formatSelectColumns(columns)).FROM(tableName);
    return this;
  }

  public SQLBuilder buildWhereClauseWithConditions(List<Condition> conditions) {
    addConditions(conditions);
    return this;
  }

  public SQLBuilder buildWhereClauseWithFieldMetadataList(List<FieldMetadata> fieldMetadataList) {
    if (!CollectionUtils.isEmpty(fieldMetadataList)) {
      fieldMetadataList.forEach(fieldMetadata -> sql.WHERE(formatAssignment(fieldMetadata)));
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

  public SQLBuilder buildPaginationClause(Pageable pageable) {
    if (Objects.isNull(pageable)) {
      return this;
    }

    final DatabaseType databaseType = SQLHelper.getDatabaseType();
    final int currentPageNumber = Math.max(1, pageable.getCurrentPageNumber());
    final int pageSize = pageable.getPageSize();

    // 计算偏移量
    final int offset = (currentPageNumber - 1) * pageSize;

    switch (databaseType) {
      case DatabaseType.MYSQL -> {
        // MySQL使用 LIMIT offset, size
        sql.LIMIT(offset + ", " + pageSize);
      }
      case DatabaseType.ORACLE -> {
        // Oracle 12c+: OFFSET n ROWS FETCH NEXT n ROWS ONLY
        paginationClause = "OFFSET %d ROWS FETCH NEXT %d ROWS ONLY".formatted(offset, pageSize);
      }
      case DatabaseType.POSTGRESQL -> {
        // PostgreSQL使用 LIMIT size OFFSET offset
        sql.LIMIT(String.valueOf(pageSize))
            .OFFSET(String.valueOf(offset));
      }
      default -> throw new UnsupportedOperationException(
          "Unsupported database type: " + databaseType);
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
    String sqlToUse;
    if (Objects.nonNull(sql)) {
      sqlToUse = sql.toString();
    } else {
      sqlToUse = sqlAsString;
    }
    if (!StringUtils.hasText(sqlToUse)) {
      throw new InvalidSqlException();
    }
    if (StringUtils.hasText(paginationClause)) {
      sqlToUse = sqlToUse + " " + paginationClause;
    }
    sqlToUse = wrapSqlWithScriptTag(sqlToUse);
    debugSql(sqlToUse);
    return sqlToUse;
  }

  // Private helper methods
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

  private String formatParameter(FieldMetadata fieldMetadata) {
    if (StringUtils.hasText(fieldMetadata.getColumnType())) {
      return "CAST(#{%s} AS %s)".formatted(fieldMetadata.getFieldName(), fieldMetadata.getColumnType());
    }
    return "#{%s}".formatted(fieldMetadata.getFieldName());
  }

  private String formatAssignment(FieldMetadata fieldMetadata) {
    return "%s = %s".formatted(fieldMetadata.getColumnName(), formatParameter(fieldMetadata));
  }

  private String formatSelectColumns(List<String> columns) {
    return CollectionUtils.isEmpty(columns) ? "*"
        : columns.stream()
            .map(SQLHelper::formatColumnName)
            .collect(Collectors.joining(", "));
  }

  private String formatOrderBy(List<SortableItem> sorting) {
    return sorting.stream()
        .map(item -> "%s %s".formatted(
            SQLHelper.formatColumnName(item.getName()),
            "DESC".equalsIgnoreCase(item.getDirection()) ? "DESC" : "ASC")
        ).collect(Collectors.joining(", "));
  }

  private boolean isPrimaryKey(FieldMetadata fieldMetadata) {
    return PRIMARY_KEY.equals(fieldMetadata.getFieldName());
  }

  private void addPrimaryKeyCondition() {
    sql.WHERE("%s = #{%s}".formatted(SQLHelper.formatColumnName(PRIMARY_KEY), PRIMARY_KEY));
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