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
    STRATEGIES.put(SQLOperator.EQ, (column, field, index) ->
        String.format("%s = #{params.%s%d}", column, field, index));

    // 不等于
    STRATEGIES.put(SQLOperator.NE, (column, field, index) ->
        String.format("%s != #{params.%s%d}", column, field, index));

    // 大于
    STRATEGIES.put(SQLOperator.GT, (column, field, index) ->
        String.format("%s > #{params.%s%d}", column, field, index));

    // 大于等于
    STRATEGIES.put(SQLOperator.GE, (column, field, index) ->
        String.format("%s >= #{params.%s%d}", column, field, index));

    // 小于
    STRATEGIES.put(SQLOperator.LT, (column, field, index) ->
        String.format("%s < #{params.%s%d}", column, field, index));

    // 小于等于
    STRATEGIES.put(SQLOperator.LE, (column, field, index) ->
        String.format("%s <= #{params.%s%d}", column, field, index));

    // 模糊查询
    STRATEGIES.put(SQLOperator.LIKE, (column, field, index) ->
        String.format("%s LIKE CONCAT('%%', #{params.%s%d}, '%%')", column, field, index));

    // 左模糊
    STRATEGIES.put(SQLOperator.STARTS_WITH, (column, field, index) ->
        String.format("%s LIKE CONCAT(#{params.%s%d}, '%%')", column, field, index));

    // 右模糊
    STRATEGIES.put(SQLOperator.ENDS_WITH, (column, field, index) ->
        String.format("%s LIKE CONCAT('%%', #{params.%s%d})", column, field, index));

    // NOT LIKE查询
    STRATEGIES.put(SQLOperator.NOT_LIKE, (column, field, index) ->
        String.format("%s NOT LIKE CONCAT('%%', #{params.%s%d}, '%%')", column, field, index));

    // IN查询
    STRATEGIES.put(SQLOperator.IN, (column, field, index) ->
        String.format("%s IN #{params.%s%d}", column, field, index));

    // NOT IN查询
    STRATEGIES.put(SQLOperator.NOT_IN, (column, field, index) ->
        String.format("%s NOT IN #{params.%s%d}", column, field, index));

    // BETWEEN查询
    STRATEGIES.put(SQLOperator.BETWEEN, (column, field, index) ->
        String.format("%s BETWEEN #{params.%s%dFirst} AND #{params.%s%dSecond}",
            column, field, index, field, index));

    // IS NULL
    STRATEGIES.put(SQLOperator.IS_NULL, (column, field, index) ->
        String.format("%s IS NULL", column));

    // IS NOT NULL
    STRATEGIES.put(SQLOperator.IS_NOT_NULL, (column, field, index) ->
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