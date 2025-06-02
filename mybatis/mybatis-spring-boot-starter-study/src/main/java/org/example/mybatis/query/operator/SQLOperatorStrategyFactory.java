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
    // 等于
    STRATEGIES.put(SQLOperator.EQ, (column, index) ->
        String.format("%s = #{params.param%d}", column, index));

    // 不等于
    STRATEGIES.put(SQLOperator.NE, (column, index) ->
        String.format("%s != #{params.param%d}", column, index));

    // 大于
    STRATEGIES.put(SQLOperator.GT, (column, index) ->
        String.format("%s > #{params.param%d}", column, index));

    // 大于等于
    STRATEGIES.put(SQLOperator.GE, (column, index) ->
        String.format("%s >= #{params.param%d}", column, index));

    // 小于
    STRATEGIES.put(SQLOperator.LT, (column, index) ->
        String.format("%s < #{params.param%d}", column, index));

    // 小于等于
    STRATEGIES.put(SQLOperator.LE, (column, index) ->
        String.format("%s <= #{params.param%d}", column, index));

    // 模糊查询
    STRATEGIES.put(SQLOperator.LIKE, (column, index) ->
        String.format("%s LIKE CONCAT('%%', #{params.param%d}, '%%')", column, index));

    // 左模糊
    STRATEGIES.put(SQLOperator.STARTS_WITH, (column, index) ->
        String.format("%s LIKE CONCAT(#{params.param%d}, '%%')", column, index));

    // 右模糊
    STRATEGIES.put(SQLOperator.ENDS_WITH, (column, index) ->
        String.format("%s LIKE CONCAT('%%', #{params.param%d})", column, index));

    // NOT LIKE查询
    STRATEGIES.put(SQLOperator.NOT_LIKE, (column, index) ->
        String.format("%s NOT LIKE CONCAT('%%', #{params.param%d}, '%%')", column, index));

    // IN查询
    STRATEGIES.put(SQLOperator.IN, (column, index) ->
        String.format("%s IN #{params.param%d}", column, index));

    // NOT IN查询
    STRATEGIES.put(SQLOperator.NOT_IN, (column, index) ->
        String.format("%s NOT IN #{params.param%d}", column, index));

    // BETWEEN查询
    STRATEGIES.put(SQLOperator.BETWEEN, (column, index) ->
        String.format("%s BETWEEN #{params.param%dFirst} AND #{params.param%dSecond}",
            column, index, index));

    // IS NULL
    STRATEGIES.put(SQLOperator.IS_NULL, (column, index) ->
        String.format("%s IS NULL", column));

    // IS NOT NULL
    STRATEGIES.put(SQLOperator.IS_NOT_NULL, (column, index) ->
        String.format("%s IS NOT NULL", column));
  }

  /**
   * 获取SQL操作符对应的策略
   */
  public static SQLOperatorStrategy getStrategy(SQLOperator operator) {
    SQLOperatorStrategy strategy = STRATEGIES.get(operator);
    if (Objects.isNull(strategy)) {
      throw new UnsupportedOperationException("Unsupported SQL operator: " + operator);
    }
    return strategy;
  }
}