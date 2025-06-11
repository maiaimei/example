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

  private SQLBuilder() {
  }

  public static SQLBuilder builder() {
    return new SQLBuilder();
  }

  public String buildInsertQuery(String tableName, List<FieldMetadata> fieldMetadataList) {
    final SQL sql = new SQL();
    sql.INSERT_INTO(tableName);
    fieldMetadataList.forEach(fieldMetadata -> sql.VALUES(fieldMetadata.getColumnName(), formatParameter(fieldMetadata)));
    return formatSql(sql.toString());
  }

  public String buildUpdateQuery(String tableName, List<FieldMetadata> fieldMetadataList) {
    final SQL sql = new SQL();
    sql.UPDATE(tableName);
    fieldMetadataList.stream()
        .filter(fieldMetadata -> !isPrimaryKey(fieldMetadata))
        .forEach(fieldMetadata -> sql.SET(formatAssignment(fieldMetadata)));
    appendPrimaryKeyCondition(sql);
    return formatSql(sql.toString());
  }

  public String buildDeleteQueryByPrimaryKey(String tableName) {
    final SQL sql = new SQL();
    sql.DELETE_FROM(tableName);
    appendPrimaryKeyCondition(sql);
    return formatSql(sql.toString());
  }

  public String buildDeleteQueryByConditions(String tableName, List<Condition> conditions) {
    final SQL sql = new SQL();
    sql.DELETE_FROM(tableName);
    appendWhereClause(sql, conditions);
    return formatSql(sql.toString());
  }

  public String buildSimpleSelectQuery(String tableName, List<FieldMetadata> fieldMetadataList) {
    final SQL sql = new SQL();
    sql.SELECT(ALL_COLUMNS).FROM(tableName);
    if (!CollectionUtils.isEmpty(fieldMetadataList)) {
      fieldMetadataList.forEach(fieldMetadata -> sql.WHERE(formatAssignment(fieldMetadata)));
    }
    return formatSql(sql.toString());
  }

  public String buildAdvancedSelectQuery(String tableName, List<String> columns, List<Condition> conditions,
      List<SortableItem> sorts) {
    final SQL sql = new SQL();
    sql.SELECT(formatSelectColumns(columns)).FROM(tableName);
    appendWhereClause(sql, conditions);
    appendOrderByClause(sql, sorts);
    return formatSql(sql.toString());
  }

  public String buildPaginationSelectQuery(String tableName, List<String> columns, List<Condition> conditions,
      List<SortableItem> sorts, Pageable pageable) {
    final SQL sql = new SQL();
    sql.SELECT(formatSelectColumns(columns)).FROM(tableName);
    appendWhereClause(sql, conditions);
    appendOrderByClause(sql, sorts);
    String sqlToUse = sql.toString();
    String paginationClause = buildPaginationClause(pageable);
    if (StringUtils.hasText(paginationClause)) {
      sqlToUse = sqlToUse + " " + paginationClause;
    }
    return formatSql(sqlToUse);
  }

  public String buildCountQuery(String tableName, List<Condition> conditions) {
    final SQL sql = new SQL();
    sql.SELECT(COUNT_COLUMN).FROM(tableName);
    appendWhereClause(sql, conditions);
    return formatSql(sql.toString());
  }

  public String buildBatchInsertQuery(String tableName, List<Field> fields, List<Object> domains) {
    final DatabaseType databaseType = SQLHelper.getDatabaseType();
    return switch (databaseType) {
      case DatabaseType.MYSQL, DatabaseType.POSTGRESQL -> buildDefaultBatchInsertQuery(tableName, fields, domains);
      case DatabaseType.ORACLE -> buildOracleBatchInsertQuery(tableName, fields, domains);
    };
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

  private String formatAssignment(FieldMetadata fieldMetadata) {
    return "%s = %s".formatted(fieldMetadata.getColumnName(), formatParameter(fieldMetadata));
  }

  private String formatParameter(FieldMetadata fieldMetadata) {
    if (StringUtils.hasText(fieldMetadata.getColumnType())) {
      return "CAST(#{%s} AS %s)".formatted(fieldMetadata.getFieldName(), fieldMetadata.getColumnType());
    }
    return "#{%s}".formatted(fieldMetadata.getFieldName());
  }

  private String formatSelectColumns(List<String> columns) {
    if (CollectionUtils.isEmpty(columns)) {
      return "*";
    }
    return columns.stream().map(SQLHelper::formatColumnName).collect(Collectors.joining(", "));
  }

  private boolean isPrimaryKey(FieldMetadata fieldMetadata) {
    return PRIMARY_KEY.equals(fieldMetadata.getFieldName());
  }

  private void appendPrimaryKeyCondition(SQL sql) {
    sql.WHERE("%s = #{%s}".formatted(SQLHelper.formatColumnName(PRIMARY_KEY), PRIMARY_KEY));
  }

  private void appendWhereClause(SQL sql, List<Condition> conditions) {
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

  private void appendOrderByClause(SQL sql, List<SortableItem> sorts) {
    if (!CollectionUtils.isEmpty(sorts)) {
      final String orderByClause = sorts.stream()
          .map(sortableItem -> {
                final String columnName = SQLHelper.formatColumnName(sortableItem.getName());
                final String columnDirection = "DESC".equalsIgnoreCase(sortableItem.getDirection()) ? "DESC" : "ASC";
                return "%s %s".formatted(columnName, columnDirection);
              }
          ).collect(Collectors.joining(", "));
      sql.ORDER_BY(orderByClause);
    }
  }

  public String buildPaginationClause(Pageable pageable) {
    if (Objects.isNull(pageable)) {
      return null;
    }

    final DatabaseType databaseType = SQLHelper.getDatabaseType();
    final int currentPageNumber = Math.max(1, pageable.getCurrentPageNumber());
    final int pageSize = Math.max(1, pageable.getPageSize());

    // 计算偏移量
    final int offset = (currentPageNumber - 1) * pageSize;

    return switch (databaseType) {
      case DatabaseType.MYSQL -> // MySQL使用 LIMIT offset, size
          "LIMIT %d, %d".formatted(offset, pageSize);
      case DatabaseType.ORACLE -> // Oracle 12c+: OFFSET n ROWS FETCH NEXT n ROWS ONLY
          "OFFSET %d ROWS FETCH NEXT %d ROWS ONLY".formatted(offset, pageSize);
      case DatabaseType.POSTGRESQL -> // PostgreSQL使用 LIMIT size OFFSET offset
          "LIMIT %d OFFSET  %d".formatted(pageSize, offset);
    };
  }

  private String formatSql(String sql) {
    if (!StringUtils.hasText(sql)) {
      throw new InvalidSqlException("SQL statement cannot be empty or null");
    }
    String sqlToUse = "<script>%s</script>".formatted(sql);
    if (log.isDebugEnabled()) {
      log.debug("==>  Preparing: {}", SqlSourceBuilder.removeExtraWhitespaces(sqlToUse));
    }
    return sqlToUse;
  }

}