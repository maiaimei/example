package org.example.mybatis;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.jdbc.SQL;
import org.example.model.PageCriteria;
import org.example.model.SortCriteria;
import org.example.mybatis.constant.DatabaseType;
import org.example.mybatis.exception.SQLBuildException;
import org.example.mybatis.model.FieldMetadata;
import org.example.mybatis.query.filter.Condition;
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

  // === Database Constants ===
  private static final String PRIMARY_KEY = "id";
  private static final String COLUMN_COUNT = "COUNT(1)";
  private static final String COLUMN_ALL = "*";

  // === SQL Keywords ===
  private static final String KEYWORD_ASC = "ASC";
  private static final String KEYWORD_DESC = "DESC";

  // === Database Specific SQL Templates ===
  private static final String TPL_INSERT_STANDARD = "INSERT INTO %s (%s) VALUES %s";
  private static final String TPL_INSERT_ORACLE = "INSERT ALL %s SELECT * FROM dual";
  private static final String TPL_INSERT_ORACLE_INTO = "INTO %s (%s) VALUES (%s) ";
  private static final String ORACLE_INSERT_ALL = "INSERT ALL";
  private static final String ORACLE_SELECT_FROM_DUAL = "SELECT * FROM dual";

  // === Pagination Templates ===
  private static final String TPL_PAGINATION_MYSQL = "LIMIT %d, %d";
  private static final String TPL_PAGINATION_ORACLE = "OFFSET %d ROWS FETCH NEXT %d ROWS ONLY";
  private static final String TPL_PAGINATION_POSTGRESQL = "LIMIT %d OFFSET %d";

  // === XML Tags ===
  private static final String XML_SCRIPT_START = "<script>";
  private static final String XML_SCRIPT_END = "</script>";

  // === Formatting Constants ===
  private static final String COMMA_SEPARATOR = ", ";
  private static final String SPACE_SEPARATOR = " ";
  private static final String PARAMETER_PLACEHOLDER = "#{%s}";
  private static final String CAST_PATTERN = "CAST(%s AS %s)";
  private static final String ASSIGNMENT_PATTERN = "%s = %s";

  // === Error Messages ===
  private static final String ERROR_TABLE_NAME_EMPTY = "Table name cannot be empty";
  private static final String ERROR_TABLE_NAME_INVALID = "Invalid table name format";
  private static final String ERROR_BATCH_SIZE_EXCEEDED = "Batch size exceeds limit of %d";
  private static final String ERROR_SQL_LENGTH_EXCEEDED = "Generated SQL exceeds maximum length of %d";
  private static final String ERROR_PAGE_SIZE_INVALID = "Page size must be positive";
  private static final String ERROR_PAGE_NUMBER_INVALID = "Page number must be positive";
  private static final String ERROR_UNSUPPORTED_DB_TYPE = "Unsupported database type: %s";

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

  public static String buildSelectQuery(String tableName, List<FieldMetadata> fieldMetadataList) {
    final SQL sql = new SQL();
    sql.SELECT(COLUMN_ALL).FROM(tableName);
    if (!CollectionUtils.isEmpty(fieldMetadataList)) {
      fieldMetadataList.forEach(fieldMetadata -> sql.WHERE(formatColumnAssignment(fieldMetadata)));
    }
    return formatSqlWithXmlWrapper(sql.toString());
  }

  public static String buildAdvancedSelectQuery(String tableName, List<String> columns, List<Condition> conditions,
      List<SortCriteria> sorts, PageCriteria page) {
    final SQL sql = new SQL();
    sql.SELECT(formatSelectColumns(columns)).FROM(tableName);
    appendWhereConditions(sql, conditions);
    appendSortingCriteria(sql, sorts);
    String sqlToUse = sql.toString();
    if (Objects.nonNull(page)) {
      String paginationClause = buildDatabaseSpecificPagination(page);
      if (StringUtils.hasText(paginationClause)) {
        sqlToUse = sqlToUse + SPACE_SEPARATOR + paginationClause;
      }
    }
    return formatSqlWithXmlWrapper(sqlToUse);
  }

  private static String buildDatabaseSpecificPagination(PageCriteria page) {
    if (Objects.isNull(page)) {
      return null;
    }

    final DatabaseType databaseType = SQLHelper.getDatabaseType();
    final int currentPageNumber = Math.max(1, page.getCurrent());
    final int pageSize = Math.max(1, page.getSize());

    // 计算偏移量
    final int offset = (currentPageNumber - 1) * pageSize;

    return switch (databaseType) {
      case DatabaseType.MYSQL -> // MySQL使用 LIMIT offset, size
          TPL_PAGINATION_MYSQL.formatted(offset, pageSize);
      case DatabaseType.ORACLE -> // Oracle 12c+: OFFSET n ROWS FETCH NEXT n ROWS ONLY
          TPL_PAGINATION_ORACLE.formatted(offset, pageSize);
      case DatabaseType.POSTGRESQL -> // PostgreSQL使用 LIMIT size OFFSET offset
          TPL_PAGINATION_POSTGRESQL.formatted(pageSize, offset);
    };
  }

  public static String buildCountQueryWithConditions(String tableName, List<Condition> conditions) {
    final SQL sql = new SQL();
    sql.SELECT(COLUMN_COUNT).FROM(tableName);
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
        .collect(Collectors.joining(COMMA_SEPARATOR));
    return TPL_INSERT_STANDARD.formatted(tableName, columnNames, values);
  }

  private static String buildOracleBatchInsert(String tableName, List<Field> fields, List<Object> domains) {
    StringBuilder sqlBuilder = new StringBuilder(ORACLE_INSERT_ALL);
    sqlBuilder.append(SPACE_SEPARATOR);
    domains.forEach(domain -> sqlBuilder.append(buildOracleInsertClause(tableName, fields, domain)));
    sqlBuilder.append(ORACLE_SELECT_FROM_DUAL);
    return sqlBuilder.toString();
  }

  private static String buildOracleInsertClause(String tableName, List<Field> fields, Object domain) {
    String columnNames = formatColumnNames(fields);
    String values = formatParameterPlaceholders(fields);
    return TPL_INSERT_ORACLE_INTO.formatted(tableName, columnNames, values);
  }

  private static String formatColumnNames(List<Field> fields) {
    return fields.stream()
        .map(Field::getName)
        .collect(Collectors.joining(COMMA_SEPARATOR));
  }

  private static String formatParameterPlaceholders(List<Field> fields) {
    return fields.stream()
        .map(field -> "#{domain.%s}".formatted(field.getName()))
        .collect(Collectors.joining(COMMA_SEPARATOR));
  }

  private static String formatColumnAssignment(FieldMetadata fieldMetadata) {
    return ASSIGNMENT_PATTERN.formatted(fieldMetadata.getColumnName(), formatParameterWithType(fieldMetadata));
  }

  private static String formatParameterWithType(FieldMetadata fieldMetadata) {
    if (StringUtils.hasText(fieldMetadata.getColumnType())) {
      return CAST_PATTERN.formatted(formatParameter(fieldMetadata.getFieldName()), fieldMetadata.getColumnType());
    }
    return formatParameter(fieldMetadata.getFieldName());
  }

  private static String formatParameter(String name) {
    return PARAMETER_PLACEHOLDER.formatted(name);
  }

  private static String formatSelectColumns(List<String> columns) {
    if (CollectionUtils.isEmpty(columns)) {
      return "*";
    }
    return columns.stream().map(SQLHelper::formatColumnName).collect(Collectors.joining(COMMA_SEPARATOR));
  }

  private static String formatSqlWithXmlWrapper(String sql) {
    if (!StringUtils.hasText(sql)) {
      throw new SQLBuildException("SQL statement cannot be empty or null");
    }
    String sqlToUse = XML_SCRIPT_START + sql + XML_SCRIPT_END;
    if (log.isDebugEnabled()) {
      log.debug("==>  Generated SQL: {}", SqlSourceBuilder.removeExtraWhitespaces(sqlToUse));
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

  private static void appendSortingCriteria(SQL sql, List<SortCriteria> sorts) {
    if (!CollectionUtils.isEmpty(sorts)) {
      final String orderByClause = sorts.stream()
          .map(sortableItem -> {
                final String columnName = SQLHelper.formatColumnName(sortableItem.getName());
                final String columnDirection = KEYWORD_DESC.equalsIgnoreCase(sortableItem.getDirection()) ? KEYWORD_DESC :
                    KEYWORD_ASC;
                return "%s %s".formatted(columnName, columnDirection);
              }
          ).collect(Collectors.joining(COMMA_SEPARATOR));
      sql.ORDER_BY(orderByClause);
    }
  }

}