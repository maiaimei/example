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
    registerComparisonOperatorStrategies();
    registerStringOperatorStrategies();
    registerOtherOperatorStrategies();
  }

  private static void registerComparisonOperatorStrategies() {
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
  }

  private static void registerStringOperatorStrategies() {
    registerStrategy(SQLOperator.LIKE, (column, index) ->
        String.format("%s LIKE CONCAT('%%', #{simpleConditions[%d].value}, '%%')", column, index));

    registerStrategy(SQLOperator.STARTS_WITH, (column, index) ->
        String.format("%s LIKE CONCAT(#{simpleConditions[%d].value}, '%%')", column, index));

    registerStrategy(SQLOperator.ENDS_WITH, (column, index) ->
        String.format("%s LIKE CONCAT('%%', #{simpleConditions[%d].value})", column, index));

    registerStrategy(SQLOperator.NOT_LIKE, (column, index) ->
        String.format("%s NOT LIKE CONCAT('%%', #{simpleConditions[%d].value}, '%%')", column, index));
  }

  private static void registerOtherOperatorStrategies() {
    registerStrategy(SQLOperator.BETWEEN, (column, index) ->
        String.format("%s BETWEEN #{simpleConditions[%d].value} AND #{simpleConditions[%d].secondValue}",
            column, index, index));

    registerStrategy(SQLOperator.IN, (column, index) ->
        String.format("%s IN #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.NOT_IN, (column, index) ->
        String.format("%s NOT IN #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.IS_NULL, (column, index) ->
        String.format("%s IS NULL", column));

    registerStrategy(SQLOperator.IS_NOT_NULL, (column, index) ->
        String.format("%s IS NOT NULL", column));
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