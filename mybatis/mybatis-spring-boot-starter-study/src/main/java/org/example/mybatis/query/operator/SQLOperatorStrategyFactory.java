package org.example.mybatis.query.operator;

import java.util.EnumMap;
import java.util.Map;

/**
 * SQL 操作符策略工厂
 */
public class SQLOperatorStrategyFactory {

  private static final Map<SQLOperator2, SQLOperatorStrategy> STRATEGIES = new EnumMap<>(SQLOperator2.class);

  static {
    // 等于
    STRATEGIES.put(SQLOperator2.EQ, (column, index) ->
        String.format("%s = #{conditions[%d].value}", column, index));

    // 不等于
    STRATEGIES.put(SQLOperator2.NE, (column, index) ->
        String.format("%s != #{conditions[%d].value}", column, index));

    // 大于
    STRATEGIES.put(SQLOperator2.GT, (column, index) ->
        String.format("%s > #{conditions[%d].value}", column, index));

    // 大于等于
    STRATEGIES.put(SQLOperator2.GE, (column, index) ->
        String.format("%s >= #{conditions[%d].value}", column, index));

    // 小于
    STRATEGIES.put(SQLOperator2.LT, (column, index) ->
        String.format("%s < #{conditions[%d].value}", column, index));

    // 小于等于
    STRATEGIES.put(SQLOperator2.LE, (column, index) ->
        String.format("%s <= #{conditions[%d].value}", column, index));

    // 模糊查询
    STRATEGIES.put(SQLOperator2.LIKE, (column, index) ->
        String.format("%s LIKE CONCAT('%%', #{conditions[%d].value}, '%%')", column, index));

    // 左模糊
    STRATEGIES.put(SQLOperator2.STARTS_WITH, (column, index) ->
        String.format("%s LIKE CONCAT(#{conditions[%d].value}, '%%')", column, index));

    // 右模糊
    STRATEGIES.put(SQLOperator2.ENDS_WITH, (column, index) ->
        String.format("%s LIKE CONCAT('%%', #{conditions[%d].value})", column, index));

    // IN查询
    STRATEGIES.put(SQLOperator2.IN, (column, index) ->
        String.format("%s IN #{conditions[%d].value}", column, index));

    // NOT IN查询
    STRATEGIES.put(SQLOperator2.NOT_IN, (column, index) ->
        String.format("%s NOT IN #{conditions[%d].value}", column, index));

    // BETWEEN查询
    STRATEGIES.put(SQLOperator2.BETWEEN, (column, index) ->
        String.format("%s BETWEEN #{conditions[%d].value} AND #{conditions[%d].secondValue}",
            column, index, index));

    // IS NULL
    STRATEGIES.put(SQLOperator2.IS_NULL, (column, index) ->
        String.format("%s IS NULL", column));

    // IS NOT NULL
    STRATEGIES.put(SQLOperator2.IS_NOT_NULL, (column, index) ->
        String.format("%s IS NOT NULL", column));
  }

  /**
   * 获取SQL操作符对应的策略
   */
  public static SQLOperatorStrategy getStrategy(SQLOperator2 operator) {
    SQLOperatorStrategy strategy = STRATEGIES.get(operator);
    if (strategy == null) {
      throw new UnsupportedOperationException("Unsupported SQL operator: " + operator);
    }
    return strategy;
  }
}