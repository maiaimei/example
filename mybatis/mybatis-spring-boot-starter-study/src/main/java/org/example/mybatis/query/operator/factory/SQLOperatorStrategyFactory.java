package org.example.mybatis.query.operator.factory;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import org.example.mybatis.query.operator.SQLOperator;
import org.example.mybatis.query.operator.SQLOperatorStrategy;

/**
 * SQL 操作符策略工厂类，用于注册和获取 SQL 操作符对应的策略。 通过该工厂类，可以根据不同的 SQL 操作符生成对应的 SQL 语句片段。
 */
public class SQLOperatorStrategyFactory {

  /**
   * 每个 IN 条件的最大元素数。
   */
  private static final int MAX_IN_SIZE = 500;

  /**
   * 存储 SQL 操作符与其对应策略的映射关系。
   */
  private static final Map<SQLOperator, SQLOperatorStrategy> STRATEGIES = new EnumMap<>(SQLOperator.class);

  /**
   * 静态代码块，用于注册所有 SQL 操作符策略。
   */
  static {
    registerComparisonOperatorStrategies();
    registerStringOperatorStrategies();
    registerRangeOperatorStrategies();
    registerOtherOperatorStrategies();
  }

  /**
   * 注册比较操作符的策略，例如 EQ（=）、NE（!=）、GT（>）等。
   */
  private static void registerComparisonOperatorStrategies() {
    registerSimpleComparisonStrategy(SQLOperator.EQ, "=");
    registerSimpleComparisonStrategy(SQLOperator.NE, "!=");
    registerSimpleComparisonStrategy(SQLOperator.GT, ">");
    registerSimpleComparisonStrategy(SQLOperator.GE, ">=");
    registerSimpleComparisonStrategy(SQLOperator.LT, "<");
    registerSimpleComparisonStrategy(SQLOperator.LE, "<=");
  }

  /**
   * 注册字符串操作符的策略，例如 LIKE、STARTS_WITH、ENDS_WITH 等。
   */
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

  /**
   * 注册范围操作符的策略，例如 BETWEEN、IN、NOT_IN 等。
   */
  private static void registerRangeOperatorStrategies() {
    registerStrategy(SQLOperator.BETWEEN, (column, index) ->
        String.format("%s BETWEEN #{simpleConditions[%d].value} AND #{simpleConditions[%d].secondValue}", column, index, index));

    registerInOperatorStrategy(SQLOperator.IN, "IN");
    registerInOperatorStrategy(SQLOperator.NOT_IN, "NOT IN");
    registerLimitedInOperatorStrategy(SQLOperator.LIMITED_IN, "IN");
    registerLimitedInOperatorStrategy(SQLOperator.LIMITED_NOT_IN, "NOT IN");
  }

  /**
   * 注册其他操作符的策略，例如 IS_NULL、IS_NOT_NULL 等。
   */
  private static void registerOtherOperatorStrategies() {
    registerStrategy(SQLOperator.IS_NULL, (column, index) ->
        String.format("%s IS NULL", column));

    registerStrategy(SQLOperator.IS_NOT_NULL, (column, index) ->
        String.format("%s IS NOT NULL", column));
  }

  /**
   * 注册简单比较操作符的策略，例如 EQ（=）、NE（!=）等。
   *
   * @param operator SQL 操作符
   * @param symbol   对应的 SQL 符号
   */
  private static void registerSimpleComparisonStrategy(SQLOperator operator, String symbol) {
    registerStrategy(operator, (column, index) ->
        String.format("%s %s #{simpleConditions[%d].value}", column, symbol, index));
  }

  /**
   * 注册 IN 操作符的策略。
   *
   * @param operator SQL 操作符
   * @param keyword  对应的 SQL 关键字，例如 "IN" 或 "NOT IN"
   */
  private static void registerInOperatorStrategy(SQLOperator operator, String keyword) {
    registerStrategy(operator, (column, index) ->
        String.format(
            "%s %s <foreach collection='simpleConditions[%d].value' item='item' open='(' separator=',' close=')'>#{item}</foreach>",
            column, keyword, index));
  }

  /**
   * 注册有限大小的 IN 操作符的策略，用于处理 IN 条件元素数量超过限制的情况。
   *
   * @param operator SQL 操作符
   * @param keyword  对应的 SQL 关键字，例如 "IN" 或 "NOT IN"
   */
  private static void registerLimitedInOperatorStrategy(SQLOperator operator, String keyword) {
    registerStrategy(operator, (column, index) -> String.format("""
            <choose>
                <when test='simpleConditions[%d].value != null and simpleConditions[%d].value.size() > %d'>
                    <trim prefix='(' prefixOverrides='OR' suffix=')'>
                        <foreach collection='simpleConditions[%d].value' item='item' open='' close='' separator='' index='i'>
                            <if test='i %% %d == 0'>
                                <choose>
                                    <when test='i == 0'>%s %s </when>
                                    <otherwise> OR %s %s </otherwise>
                                </choose>
                            </if>
                            <if test='i %% %d == 0'>(</if>
                            #{item}
                            <if test='i %% %d == %d or i == simpleConditions[%d].value.size() - 1'>)</if>
                            <if test='i %% %d != %d and i != simpleConditions[%d].value.size() - 1'>,</if>
                        </foreach>
                    </trim>
                </when>
                <otherwise>
                    %s %s <foreach collection='simpleConditions[%d].value' item='item' open='(' separator=',' close=')'>#{item}</foreach>
                </otherwise>
            </choose>""",
        index, index, MAX_IN_SIZE,
        index,
        MAX_IN_SIZE, column, keyword, column, keyword,
        MAX_IN_SIZE,
        MAX_IN_SIZE, MAX_IN_SIZE - 1, index,
        MAX_IN_SIZE, MAX_IN_SIZE - 1, index,
        column, keyword, index));
  }

  /**
   * 注册自定义 SQL 操作符策略。
   *
   * @param operator SQL 操作符
   * @param strategy 对应的策略
   * @throws IllegalArgumentException 如果操作符或策略为 null
   */
  public static void registerStrategy(SQLOperator operator, SQLOperatorStrategy strategy) {
    if (Objects.isNull(operator) || Objects.isNull(strategy)) {
      throw new IllegalArgumentException("Operator and strategy must not be null.");
    }
    STRATEGIES.put(operator, strategy);
  }

  /**
   * 获取指定 SQL 操作符对应的策略。
   *
   * @param operator SQL 操作符
   * @return 对应的策略
   * @throws UnsupportedOperationException 如果操作符未注册
   */
  public static SQLOperatorStrategy getStrategy(SQLOperator operator) {
    SQLOperatorStrategy strategy = STRATEGIES.get(operator);
    if (Objects.isNull(strategy)) {
      throw new UnsupportedOperationException("Unsupported SQL operator: " + operator);
    }
    return strategy;
  }
}