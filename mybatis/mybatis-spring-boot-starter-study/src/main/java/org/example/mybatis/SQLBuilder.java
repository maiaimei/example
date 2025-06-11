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
import org.example.mybatis.exception.SQLBuildException;
import org.example.mybatis.model.FieldMetadata;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.page.Pageable;
import org.example.mybatis.query.sort.SortableItem;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Utility class for building SQL queries. This class provides static methods to generate various types of SQL queries including
 * INSERT, UPDATE, DELETE, and SELECT statements.
 *
 * <p>Example usage:</p>
 * <pre>
 * String insertSql = SQLBuilder.buildInsertQuery("users", fieldMetadataList);
 * String selectSql = SQLBuilder.buildPaginationSelectQuery("users", columns, conditions, sorts, pageable);
 * </pre>
 *
 * <p>Note: This class is thread-safe as it contains only static methods and immutable state.</p>
 */
@Slf4j
public final class SQLBuilder {

  private static final String PRIMARY_KEY = "id";
  private static final String COUNT_COLUMN = "COUNT(1)";
  private static final String ALL_COLUMNS = "*";

  private SQLBuilder() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static String buildInsertQuery(String tableName, List<FieldMetadata> fieldMetadataList) {
    final SQL sql = new SQL();
    sql.INSERT_INTO(tableName);
    fieldMetadataList.forEach(fieldMetadata -> sql.VALUES(fieldMetadata.getColumnName(), formatParameterWithType(fieldMetadata)));
    return formatSqlWithXmlWrapper(sql.toString());
  }

  public static String buildUpdateQueryByPrimaryKey(String tableName, List<FieldMetadata> fieldMetadataList) {
    final SQL sql = new SQL();
    sql.UPDATE(tableName);
    fieldMetadataList.stream()
        .filter(fieldMetadata -> !isPrimaryKey(fieldMetadata))
        .forEach(fieldMetadata -> sql.SET(formatColumnAssignment(fieldMetadata)));
    appendPrimaryKeyCondition(sql);
    return formatSqlWithXmlWrapper(sql.toString());
  }

  public static String buildDeleteQueryByPrimaryKey(String tableName) {
    final SQL sql = new SQL();
    sql.DELETE_FROM(tableName);
    appendPrimaryKeyCondition(sql);
    return formatSqlWithXmlWrapper(sql.toString());
  }

  public static String buildDeleteQueryWithConditions(String tableName, List<Condition> conditions) {
    final SQL sql = new SQL();
    sql.DELETE_FROM(tableName);
    appendWhereConditions(sql, conditions);
    return formatSqlWithXmlWrapper(sql.toString());
  }

  public static String buildSelectQueryWithFieldConditions(String tableName, List<FieldMetadata> fieldMetadataList) {
    final SQL sql = new SQL();
    sql.SELECT(ALL_COLUMNS).FROM(tableName);
    if (!CollectionUtils.isEmpty(fieldMetadataList)) {
      fieldMetadataList.forEach(fieldMetadata -> sql.WHERE(formatColumnAssignment(fieldMetadata)));
    }
    return formatSqlWithXmlWrapper(sql.toString());
  }

  public static String buildSelectQueryWithConditions(String tableName, List<Condition> conditions) {
    final SQL sql = new SQL();
    sql.SELECT(ALL_COLUMNS).FROM(tableName);
    appendWhereConditions(sql, conditions);
    return formatSqlWithXmlWrapper(sql.toString());
  }

  public static String buildSelectQueryWithColumnsAndConditions(String tableName, List<String> columns,
      List<Condition> conditions) {
    final SQL sql = new SQL();
    sql.SELECT(formatSelectColumns(columns)).FROM(tableName);
    appendWhereConditions(sql, conditions);
    return formatSqlWithXmlWrapper(sql.toString());
  }

  public static String buildSelectQueryWithSort(String tableName, List<String> columns, List<Condition> conditions,
      List<SortableItem> sorts) {
    final SQL sql = new SQL();
    sql.SELECT(formatSelectColumns(columns)).FROM(tableName);
    appendWhereConditions(sql, conditions);
    appendSortingCriteria(sql, sorts);
    return formatSqlWithXmlWrapper(sql.toString());
  }

  public static String buildSelectQueryWithPagination(String tableName, List<String> columns, List<Condition> conditions,
      List<SortableItem> sorts, Pageable pageable) {
    final SQL sql = new SQL();
    sql.SELECT(formatSelectColumns(columns)).FROM(tableName);
    appendWhereConditions(sql, conditions);
    appendSortingCriteria(sql, sorts);
    String sqlToUse = sql.toString();
    String paginationClause = buildDatabaseSpecificPagination(pageable);
    if (StringUtils.hasText(paginationClause)) {
      sqlToUse = sqlToUse + " " + paginationClause;
    }
    return formatSqlWithXmlWrapper(sqlToUse);
  }

  private static String buildDatabaseSpecificPagination(Pageable pageable) {
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

  public static String buildCountQueryWithConditions(String tableName, List<Condition> conditions) {
    final SQL sql = new SQL();
    sql.SELECT(COUNT_COLUMN).FROM(tableName);
    appendWhereConditions(sql, conditions);
    return formatSqlWithXmlWrapper(sql.toString());
  }

  public static String buildDatabaseSpecificBatchInsert(String tableName, List<Field> fields, List<Object> domains) {
    final DatabaseType databaseType = SQLHelper.getDatabaseType();
    return switch (databaseType) {
      case DatabaseType.MYSQL, DatabaseType.POSTGRESQL -> buildStandardBatchInsert(tableName, fields, domains);
      case DatabaseType.ORACLE -> buildOracleBatchInsert(tableName, fields, domains);
    };
  }

  // Private helper methods
  private static String buildStandardBatchInsert(String tableName, List<Field> fields, List<Object> domains) {
    String columnNames = formatColumnNames(fields);
    String values = domains.stream()
        .map(domain -> formatParameterPlaceholders(fields))
        .collect(Collectors.joining(", "));
    return "INSERT INTO %s (%s) VALUES %s".formatted(tableName, columnNames, values);
  }

  private static String buildOracleBatchInsert(String tableName, List<Field> fields, List<Object> domains) {
    StringBuilder sqlBuilder = new StringBuilder("INSERT ALL ");
    domains.forEach(domain -> sqlBuilder.append(buildOracleInsertClause(tableName, fields, domain)));
    sqlBuilder.append("SELECT * FROM dual");
    return sqlBuilder.toString();
  }

  private static String buildOracleInsertClause(String tableName, List<Field> fields, Object domain) {
    String columnNames = formatColumnNames(fields);
    String values = formatParameterPlaceholders(fields);
    return "INTO %s (%s) VALUES (%s) ".formatted(tableName, columnNames, values);
  }

  private static String formatColumnNames(List<Field> fields) {
    return fields.stream()
        .map(Field::getName)
        .collect(Collectors.joining(", "));
  }

  private static String formatParameterPlaceholders(List<Field> fields) {
    return fields.stream()
        .map(field -> "#{domain.%s}".formatted(field.getName()))
        .collect(Collectors.joining(", "));
  }

  private static String formatColumnAssignment(FieldMetadata fieldMetadata) {
    return "%s = %s".formatted(fieldMetadata.getColumnName(), formatParameterWithType(fieldMetadata));
  }

  private static String formatParameterWithType(FieldMetadata fieldMetadata) {
    if (StringUtils.hasText(fieldMetadata.getColumnType())) {
      return "CAST(#{%s} AS %s)".formatted(fieldMetadata.getFieldName(), fieldMetadata.getColumnType());
    }
    return "#{%s}".formatted(fieldMetadata.getFieldName());
  }

  private static String formatSelectColumns(List<String> columns) {
    if (CollectionUtils.isEmpty(columns)) {
      return "*";
    }
    return columns.stream().map(SQLHelper::formatColumnName).collect(Collectors.joining(", "));
  }

  private static String formatSqlWithXmlWrapper(String sql) {
    if (!StringUtils.hasText(sql)) {
      throw new SQLBuildException("SQL statement cannot be empty or null");
    }
    String sqlToUse = "<script>%s</script>".formatted(sql);
    if (log.isDebugEnabled()) {
      log.debug("==>  Preparing: {}", SqlSourceBuilder.removeExtraWhitespaces(sqlToUse));
    }
    return sqlToUse;
  }

  private static boolean isPrimaryKey(FieldMetadata fieldMetadata) {
    return PRIMARY_KEY.equals(fieldMetadata.getFieldName());
  }

  private static void appendPrimaryKeyCondition(SQL sql) {
    sql.WHERE("%s = #{%s}".formatted(SQLHelper.formatColumnName(PRIMARY_KEY), PRIMARY_KEY));
  }

  private static void appendWhereConditions(SQL sql, List<Condition> conditions) {
    if (!CollectionUtils.isEmpty(conditions)) {
      AtomicInteger index = new AtomicInteger(0);
      try {
        conditions.forEach(condition -> {
          if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
          }
          String whereSql = condition.build(index);
          if (StringUtils.hasText(whereSql)) {
            sql.WHERE(whereSql);
          }
        });
      } catch (Exception e) {
        throw new SQLBuildException("Error building WHERE clause", e);
      }
    }
  }

  private static void appendSortingCriteria(SQL sql, List<SortableItem> sorts) {
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

}