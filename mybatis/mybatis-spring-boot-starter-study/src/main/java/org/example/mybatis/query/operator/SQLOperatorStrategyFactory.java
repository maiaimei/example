package org.example.mybatis.query.operator;

import static org.example.mybatis.query.operator.SQLOperatorFormat.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * SQL 操作符策略工厂类，用于注册和获取 SQL 操作符对应的策略。 通过该工厂类，可以根据不同的 SQL 操作符生成对应的 SQL 语句片段。
 */
public class SQLOperatorStrategyFactory {

  // 每个 IN 条件的最大元素数。
  private static final int MAX_IN_SIZE = 500;

  // 存储 SQL 操作符与其对应策略的映射关系。
  private static final Map<SQLOperator, SQLOperatorStrategy> STRATEGIES = new EnumMap<>(SQLOperator.class);


  // 静态代码块，用于注册所有 SQL 操作符策略。
  static {
    registerComparisonOperatorStrategies();
    registerStringOperatorStrategies();
    registerRangeOperatorStrategies();
    registerJsonOperatorStrategies();
    registerArrayOperatorStrategies();
    registerOtherOperatorStrategies();
  }

  /**
   * 注册比较操作符的策略，例如 EQ（=）、NE（!=）、GT（>）等。
   */
  private static void registerComparisonOperatorStrategies() {
    registerStrategy(SQLOperator.EQ, EQ_FORMAT);
    registerStrategy(SQLOperator.NE, NE_FORMAT);
    registerStrategy(SQLOperator.GT, GT_FORMAT);
    registerStrategy(SQLOperator.GE, GE_FORMAT);
    registerStrategy(SQLOperator.LT, LT_FORMAT);
    registerStrategy(SQLOperator.LE, LE_FORMAT);
  }

  /**
   * 注册字符串操作符的策略，例如 LIKE、STARTS_WITH、ENDS_WITH 等。
   */
  private static void registerStringOperatorStrategies() {
    registerStrategy(SQLOperator.LIKE, LIKE_FORMAT);
    registerStrategy(SQLOperator.STARTS_WITH, STARTS_WITH_FORMAT);
    registerStrategy(SQLOperator.ENDS_WITH, ENDS_WITH_FORMAT);
    registerStrategy(SQLOperator.NOT_LIKE, NOT_LIKE_FORMAT);

    registerStrategy(SQLOperator.LIKE_CASE_INSENSITIVE, LIKE_CASE_INSENSITIVE_FORMAT);
    registerStrategy(SQLOperator.NOT_LIKE_CASE_INSENSITIVE, NOT_LIKE_CASE_INSENSITIVE_FORMAT);
    registerStrategy(SQLOperator.SIMILAR_TO, SIMILAR_TO_FORMAT);
    registerStrategy(SQLOperator.NOT_SIMILAR_TO, NOT_SIMILAR_TO_FORMAT);
    registerStrategy(SQLOperator.REGEX_MATCH, REGEX_MATCH_FORMAT);
    registerStrategy(SQLOperator.REGEX_MATCH_CASE_INSENSITIVE, REGEX_MATCH_CASE_INSENSITIVE_FORMAT);
    registerStrategy(SQLOperator.REGEX_NOT_MATCH, REGEX_NOT_MATCH_FORMAT);
    registerStrategy(SQLOperator.REGEX_NOT_MATCH_CASE_INSENSITIVE, REGEX_NOT_MATCH_CASE_INSENSITIVE_FORMAT);
  }

  /**
   * 注册范围操作符的策略，例如 BETWEEN、IN、NOT_IN 等。
   */
  private static void registerRangeOperatorStrategies() {
    registerStrategy(SQLOperator.BETWEEN,
        (condition, index) -> String.format(BETWEEN_FORMAT, condition.getColumnName(), index, index));
    registerInOperatorStrategy(SQLOperator.IN, "IN");
    registerInOperatorStrategy(SQLOperator.NOT_IN, "NOT IN");

    registerStrategy(SQLOperator.RANGE_CONTAINS, RANGE_CONTAINS_FORMAT);
    registerStrategy(SQLOperator.RANGE_CONTAINED_BY, RANGE_CONTAINED_BY_FORMAT);
    registerStrategy(SQLOperator.RANGE_OVERLAP, RANGE_OVERLAP_FORMAT);
    registerStrategy(SQLOperator.RANGE_LEFT, RANGE_LEFT_FORMAT);
    registerStrategy(SQLOperator.RANGE_RIGHT, RANGE_RIGHT_FORMAT);
    registerStrategy(SQLOperator.RANGE_ADJACENT, RANGE_ADJACENT_FORMAT);
  }

  /**
   * 注册 IN 操作符的策略。
   *
   * @param operator SQL 操作符
   * @param keyword  对应的 SQL 关键字，例如 "IN" 或 "NOT IN"
   */
  private static void registerInOperatorStrategy(SQLOperator operator, String keyword) {
    registerStrategy(operator, (condition, index) -> String.format(IN_FORMAT,
        index, index, MAX_IN_SIZE,
        index,
        MAX_IN_SIZE, condition.getColumnName(), keyword, condition.getColumnName(), keyword,
        MAX_IN_SIZE,
        MAX_IN_SIZE, MAX_IN_SIZE - 1, index,
        MAX_IN_SIZE, MAX_IN_SIZE - 1, index,
        condition.getColumnName(), keyword, index));
  }

  private static void registerJsonOperatorStrategies() {
    registerStrategy(SQLOperator.JSON_CONTAINS, JSON_CONTAINS_FORMAT);
    registerStrategy(SQLOperator.JSON_CONTAINED_BY, JSON_CONTAINED_BY_FORMAT);
    registerStrategy(SQLOperator.JSONB_TEXT_EQUALS,
        ((condition, index) -> String.format(
            JSONB_TEXT_EQ_FORMAT.replace("#{jsonPath}", condition.getMap().get("jsonPath").toString()),
            condition.getColumnName(), index)));
    registerStrategy(SQLOperator.JSONB_TEXT_LIKE, JSONB_TEXT_LIKE_FORMAT);
    registerStrategy(SQLOperator.JSONB_TEXT_NOT_LIKE, JSONB_TEXT_NOT_LIKE_FORMAT);
  }

  private static void registerArrayOperatorStrategies() {
    registerStrategy(SQLOperator.ARRAY_EQUALS, ARRAY_EQUALS_FORMAT);
    registerStrategy(SQLOperator.ARRAY_NOT_EQUALS, ARRAY_NOT_EQUALS_FORMAT);
    registerStrategy(SQLOperator.ARRAY_CONTAINS, ARRAY_CONTAINS_FORMAT);
    registerStrategy(SQLOperator.ARRAY_CONTAINED_BY, ARRAY_CONTAINED_BY_FORMAT);
    registerStrategy(SQLOperator.ARRAY_OVERLAP, ARRAY_OVERLAP_FORMAT);
  }

  /**
   * 注册其他操作符的策略，例如 IS_NULL、IS_NOT_NULL 等。
   */
  private static void registerOtherOperatorStrategies() {
    registerStrategy(SQLOperator.IS_NULL, IS_NULL_FORMAT);
    registerStrategy(SQLOperator.IS_NOT_NULL, IS_NOT_NULL_FORMAT);
    registerStrategy(SQLOperator.IS_DISTINCT_FROM, IS_DISTINCT_FROM_FORMAT);
    registerStrategy(SQLOperator.IS_NOT_DISTINCT_FROM, IS_NOT_DISTINCT_FROM_FORMAT);
  }

  /**
   * 注册自定义 SQL 操作符策略。
   *
   * @param operator SQL 操作符
   * @param format   格式化字符串，用于生成 SQL 片段
   */
  public static void registerStrategy(SQLOperator operator, String format) {
    registerStrategy(operator, (condition, index) -> String.format(format, condition.getColumnName(), index));
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
      throw new UnsupportedOperationException("Unsupported SQL operator: %s".formatted(operator));
    }
    return strategy;
  }
}