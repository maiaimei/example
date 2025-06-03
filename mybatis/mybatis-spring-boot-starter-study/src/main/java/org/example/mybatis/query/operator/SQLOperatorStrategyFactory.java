package org.example.mybatis.query.operator;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * SQL 操作符策略工厂
 */
public class SQLOperatorStrategyFactory {

  private static final Map<SQLOperator, SQLOperatorStrategy> STRATEGIES = new EnumMap<>(SQLOperator.class);

  static {
    registerDefaultStrategies();
    registerPostgreSQLStrategies();
  }

  /**
   * 注册默认的 SQL 操作符策略
   */
  private static void registerDefaultStrategies() {
    registerStrategy(SQLOperator.EQ, (column, index) ->
        String.format("%s = #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.NE, (column, index) ->
        String.format("%s != #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.GT, (column, index) ->
        String.format("%s > #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.GE, (column, index) ->
        String.format("%s >= #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.LT, (column, index) ->
        String.format("%s < #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.LE, (column, index) ->
        String.format("%s <= #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.LIKE, (column, index) ->
        String.format("%s LIKE CONCAT('%%', #{simpleConditions[%d].value}, '%%')", column, index));

    registerStrategy(SQLOperator.STARTS_WITH, (column, index) ->
        String.format("%s LIKE CONCAT(#{simpleConditions[%d].value}, '%%')", column, index));

    registerStrategy(SQLOperator.ENDS_WITH, (column, index) ->
        String.format("%s LIKE CONCAT('%%', #{simpleConditions[%d].value})", column, index));

    registerStrategy(SQLOperator.NOT_LIKE, (column, index) ->
        String.format("%s NOT LIKE CONCAT('%%', #{simpleConditions[%d].value}, '%%')", column, index));

    registerStrategy(SQLOperator.IN, (column, index) ->
        String.format("%s IN #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.NOT_IN, (column, index) ->
        String.format("%s NOT IN #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.BETWEEN, (column, index) ->
        String.format("%s BETWEEN #{simpleConditions[%d].value} AND #{simpleConditions[%d].secondValue}",
            column, index, index));

    registerStrategy(SQLOperator.IS_NULL, (column, index) ->
        String.format("%s IS NULL", column));

    registerStrategy(SQLOperator.IS_NOT_NULL, (column, index) ->
        String.format("%s IS NOT NULL", column));
  }

  /**
   * 注册 PostgreSQL 特有的 SQL 操作符策略
   */
  private static void registerPostgreSQLStrategies() {
    // String Operators
    registerStrategy(SQLOperator.ILIKE, (column, index) ->
        String.format("%s ILIKE CONCAT('%%', #{simpleConditions[%d].value}, '%%')", column, index));

    registerStrategy(SQLOperator.NOT_ILIKE, (column, index) ->
        String.format("%s NOT ILIKE CONCAT('%%', #{simpleConditions[%d].value}, '%%')", column, index));

    registerStrategy(SQLOperator.SIMILAR_TO, (column, index) ->
        String.format("%s SIMILAR TO #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.NOT_SIMILAR_TO, (column, index) ->
        String.format("%s NOT SIMILAR TO #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.REGEX_MATCH, (column, index) ->
        String.format("%s ~ #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.REGEX_MATCH_CASE_INSENSITIVE, (column, index) ->
        String.format("%s ~* #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.REGEX_NOT_MATCH, (column, index) ->
        String.format("%s !~ #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.REGEX_NOT_MATCH_CASE_INSENSITIVE, (column, index) ->
        String.format("%s !~* #{simpleConditions[%d].value}", column, index));

    // JSON Operators
    registerStrategy(SQLOperator.JSON_CONTAINS, (column, index) ->
        String.format("%s @> #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.JSON_CONTAINED_BY, (column, index) ->
        String.format("%s <@ #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.JSON_EXISTS, (column, index) ->
        String.format("%s ? #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.JSON_EXISTS_ANY, (column, index) ->
        String.format("%s ?| #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.JSON_EXISTS_ALL, (column, index) ->
        String.format("%s ?& #{simpleConditions[%d].value}", column, index));

    // Array Operators
    registerStrategy(SQLOperator.ARRAY_EQUALS, (column, index) ->
        String.format("%s = #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.ARRAY_NOT_EQUALS, (column, index) ->
        String.format("%s <> #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.ARRAY_CONTAINS, (column, index) ->
        String.format("%s @> #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.ARRAY_CONTAINED_BY, (column, index) ->
        String.format("%s <@ #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.ARRAY_OVERLAP, (column, index) ->
        String.format("%s && #{simpleConditions[%d].value}", column, index));

    // Range Operators
    registerStrategy(SQLOperator.RANGE_CONTAINS, (column, index) ->
        String.format("%s @> #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.RANGE_CONTAINED_BY, (column, index) ->
        String.format("%s <@ #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.RANGE_OVERLAP, (column, index) ->
        String.format("%s && #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.RANGE_LEFT, (column, index) ->
        String.format("%s << #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.RANGE_RIGHT, (column, index) ->
        String.format("%s >> #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.RANGE_ADJACENT, (column, index) ->
        String.format("%s -|- #{simpleConditions[%d].value}", column, index));

    // Distinct Operators
    registerStrategy(SQLOperator.IS_DISTINCT_FROM, (column, index) ->
        String.format("%s IS DISTINCT FROM #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.IS_NOT_DISTINCT_FROM, (column, index) ->
        String.format("%s IS NOT DISTINCT FROM #{simpleConditions[%d].value}", column, index));
  }

  /**
   * 注册自定义 SQL 操作符策略
   *
   * @param operator SQL 操作符
   * @param strategy 对应的策略
   */
  public static void registerStrategy(SQLOperator operator, SQLOperatorStrategy strategy) {
    if (Objects.isNull(operator) || Objects.isNull(strategy)) {
      throw new IllegalArgumentException("Operator and strategy must not be null.");
    }
    STRATEGIES.put(operator, strategy);
  }

  /**
   * 获取 SQL 操作符对应的策略
   *
   * @param operator SQL 操作符
   * @return 对应的策略
   */
  public static SQLOperatorStrategy getStrategy(SQLOperator operator) {
    SQLOperatorStrategy strategy = STRATEGIES.get(operator);
    if (Objects.isNull(strategy)) {
      throw new UnsupportedOperationException("Unsupported SQL operator: " + operator);
    }
    return strategy;
  }
}