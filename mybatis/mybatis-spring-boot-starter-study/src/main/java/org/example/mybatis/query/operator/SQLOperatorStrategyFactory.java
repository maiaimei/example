package org.example.mybatis.query.operator;

import static org.example.mybatis.query.operator.SQLOperatorFormat.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

/**
 * SQL 操作符策略工厂类，用于注册和获取 SQL 操作符对应的策略。 通过该工厂类，可以根据不同的 SQL 操作符生成对应的 SQL 语句片段。
 */
public class SQLOperatorStrategyFactory {

  private static final Pattern CONDITION_INDEX_PATTERN = Pattern.compile("simpleConditions\\[\\$\\{index}]");
  private static final String CONDITION_INDEX_FORMAT = "simpleConditions[%d]";
  private static final String MAX_SIZE = "maxSize";
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
    registerStrategy(SQLOperator.EQ, COMPARISON_OPERATOR_FORMAT);
    registerStrategy(SQLOperator.NE, COMPARISON_OPERATOR_FORMAT);
    registerStrategy(SQLOperator.GT, COMPARISON_OPERATOR_FORMAT);
    registerStrategy(SQLOperator.GE, COMPARISON_OPERATOR_FORMAT);
    registerStrategy(SQLOperator.LT, COMPARISON_OPERATOR_FORMAT);
    registerStrategy(SQLOperator.LE, COMPARISON_OPERATOR_FORMAT);
  }

  /**
   * 注册字符串操作符的策略，例如 LIKE、STARTS_WITH、ENDS_WITH 等。
   */
  private static void registerStringOperatorStrategies() {
    registerStrategy(SQLOperator.LIKE, LIKE_FORMAT);
    registerStrategy(SQLOperator.LIKE_CASE_INSENSITIVE, LIKE_FORMAT);
    registerStrategy(SQLOperator.NOT_LIKE, LIKE_FORMAT);
    registerStrategy(SQLOperator.NOT_LIKE_CASE_INSENSITIVE, LIKE_FORMAT);
    registerStrategy(SQLOperator.STARTS_WITH, STARTS_WITH_FORMAT);
    registerStrategy(SQLOperator.ENDS_WITH, ENDS_WITH_FORMAT);
  }

  /**
   * 注册范围操作符的策略，例如 BETWEEN、IN、NOT_IN 等。
   */
  private static void registerRangeOperatorStrategies() {
    registerStrategy(SQLOperator.BETWEEN, BETWEEN_FORMAT);
    registerInOperatorStrategy(SQLOperator.IN);
    registerInOperatorStrategy(SQLOperator.NOT_IN);
  }

  /**
   * 注册 IN 操作符的策略。
   *
   * @param operator SQL 操作符
   */
  private static void registerInOperatorStrategy(SQLOperator operator) {
    registerStrategy(operator, (condition, index) -> {
      condition.setParameters(Map.of(MAX_SIZE, MAX_IN_SIZE));
      return replaceIndex(IN_FORMAT, index);
    });
  }

  private static void registerJsonOperatorStrategies() {
    registerStrategy(SQLOperator.JSONB_TEXT_EQUALS, JSONB_TEXT_EQ_FORMAT);
    registerStrategy(SQLOperator.JSONB_TEXT_LIKE, JSONB_TEXT_LIKE_FORMAT);
    registerStrategy(SQLOperator.JSONB_TEXT_NOT_LIKE, JSONB_TEXT_NOT_LIKE_FORMAT);
    registerStrategy(SQLOperator.JSONB_ARRAY_CONTAINS, JSONB_ARRAY_CONTAINS_FORMAT);
    registerStrategy(SQLOperator.JSONB_ARRAY_LIKE, JSONB_ARRAY_LIKE_FORMAT);
    registerStrategy(SQLOperator.JSONB_OBJECT_ARRAY_EQUALS, JSONB_OBJECT_ARRAY_EQ_FORMAT);
    registerStrategy(SQLOperator.JSONB_OBJECT_ARRAY_LIKE, JSONB_OBJECT_ARRAY_LIKE_FORMAT);
  }

  private static void registerArrayOperatorStrategies() {
    registerStrategy(SQLOperator.ARRAY_CONTAINS, ARRAY_CONTAINS_FORMAT);
  }

  /**
   * 注册其他操作符的策略，例如 IS_NULL、IS_NOT_NULL 等。
   */
  private static void registerOtherOperatorStrategies() {
    registerStrategy(SQLOperator.IS_NULL, NULL_FORMAT);
    registerStrategy(SQLOperator.IS_NOT_NULL, NULL_FORMAT);
  }

  /**
   * 注册自定义 SQL 操作符策略。
   *
   * @param operator SQL 操作符
   * @param format   格式化字符串，用于生成 SQL 片段
   */
  public static void registerStrategy(SQLOperator operator, String format) {
    registerStrategy(operator, (condition, index) -> replaceIndex(format, index));
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

  private static String replaceIndex(String input, int index) {
    if (StringUtils.hasText(input)) {
      Matcher matcher = CONDITION_INDEX_PATTERN.matcher(input);
      String replacement = String.format(CONDITION_INDEX_FORMAT, index);
      return matcher.replaceAll(replacement);
    }
    return input;
  }

}