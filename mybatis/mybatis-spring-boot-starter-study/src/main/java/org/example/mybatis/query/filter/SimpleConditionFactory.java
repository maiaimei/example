package org.example.mybatis.query.filter;

import java.util.Map;
import java.util.Objects;
import org.example.mybatis.query.operator.SQLOperator;
import org.springframework.util.StringUtils;

public class SimpleConditionFactory {

  private static final String START_VALUE = "startValue";
  private static final String END_VALUE = "endValue";
  private static final String JSON_PATH = "jsonPath";
  private static final String NESTED_FIELD = "nestedField";

  public static SimpleCondition eq(String field, Object value) {
    return newSimpleCondition(field, SQLOperator.EQ, value);
  }

  public static SimpleCondition like(String field, String value) {
    return newSimpleCondition(field, SQLOperator.LIKE, value);
  }

  public static SimpleCondition ilike(String field, String value) {
    return newSimpleCondition(field, SQLOperator.LIKE_CASE_INSENSITIVE, value);
  }

  public static SimpleCondition startsWith(String field, String value) {
    return newSimpleCondition(field, SQLOperator.STARTS_WITH, value);
  }

  public static SimpleCondition endsWith(String field, String value) {
    return newSimpleCondition(field, SQLOperator.ENDS_WITH, value);
  }

  public static SimpleCondition between(String field, Object startValue, Object endValue) {
    return newSimpleCondition(field, SQLOperator.BETWEEN, Map.of(START_VALUE, startValue, END_VALUE, endValue));
  }

  public static SimpleCondition in(String field, Object value) {
    return newSimpleCondition(field, SQLOperator.IN, value);
  }

  public static SimpleCondition jsonbTextEquals(String field, String value, String jsonPath) {
    return newSimpleCondition(field, SQLOperator.JSONB_TEXT_EQUALS, value, Map.of(JSON_PATH, jsonPath));
  }

  public static SimpleCondition jsonbTextLike(String field, String value, String jsonPath) {
    return newSimpleCondition(field, SQLOperator.JSONB_TEXT_LIKE, value, Map.of(JSON_PATH, jsonPath));
  }

  public static SimpleCondition jsonbTextNotLike(String field, String value, String jsonPath) {
    return newSimpleCondition(field, SQLOperator.JSONB_TEXT_NOT_LIKE, value, Map.of(JSON_PATH, jsonPath));
  }

  public static SimpleCondition jsonbArrayContains(String field, String value, String jsonPath) {
    return newSimpleCondition(field, SQLOperator.JSONB_ARRAY_CONTAINS, value, Map.of(JSON_PATH, jsonPath));
  }

  public static SimpleCondition jsonbArrayLike(String field, String value, String jsonPath) {
    return newSimpleCondition(field, SQLOperator.JSONB_ARRAY_LIKE, value, Map.of(JSON_PATH, jsonPath));
  }

  public static SimpleCondition jsonbObjectArrayEquals(String field, String value, String jsonPath, String nestedField) {
    return newSimpleCondition(field, SQLOperator.JSONB_OBJECT_ARRAY_EQUALS, value,
        Map.of(JSON_PATH, jsonPath, NESTED_FIELD, nestedField));
  }

  public static SimpleCondition jsonbObjectArrayLike(String field, String value, String jsonPath, String nestedField) {
    return newSimpleCondition(field, SQLOperator.JSONB_OBJECT_ARRAY_LIKE, value,
        Map.of(JSON_PATH, jsonPath, NESTED_FIELD, nestedField));
  }

  public static SimpleCondition jsonContains(String field, String value) {
    return newSimpleCondition(field, SQLOperator.JSON_CONTAINS, value);
  }

  public static SimpleCondition where(String field, SQLOperator operator, Object value) {
    return newSimpleCondition(field, operator, value);
  }

  private static SimpleCondition newSimpleCondition(String field, SQLOperator operator, Object value) {
    return newSimpleCondition(field, operator, value, null);
  }

  private static SimpleCondition newSimpleCondition(String field, SQLOperator operator, Object value,
      Map<String, Object> parameters) {
    if (Objects.isNull(value)) {
      return null;
    }
    validateParameters(field, operator, value);
    return new SimpleCondition(field, operator, value, parameters);
  }

  private static void validateParameters(String field, SQLOperator operator, Object value) {
    if (!StringUtils.hasText(field)) {
      throw new IllegalArgumentException("Field name cannot be null or empty");
    }

    if (Objects.isNull(operator)) {
      throw new IllegalArgumentException("Operator cannot be null");
    }

    switch (operator) {
      case LIKE, LIKE_CASE_INSENSITIVE, STARTS_WITH, ENDS_WITH:
        if (!(value instanceof String)) {
          throw new IllegalArgumentException(
              String.format("Operator %s requires String value, but got %s",
                  operator, value.getClass().getSimpleName()));
        }
        break;

      case BETWEEN:
        if (!(value instanceof Map<?, ?> betweenMap)) {
          throw new IllegalArgumentException("BETWEEN operator requires Map value");
        }
        if (!betweenMap.containsKey(START_VALUE) ||
            !betweenMap.containsKey(END_VALUE)) {
          throw new IllegalArgumentException(
              "BETWEEN operator requires both startValue and endValue");
        }
        break;

      case IN:
        if (!(value instanceof Iterable<?> || value.getClass().isArray())) {
          throw new IllegalArgumentException(
              "IN operator requires Iterable or Array value");
        }
        break;

      case JSON_CONTAINS:
        if (!(value instanceof String)) {
          throw new IllegalArgumentException(
              "JSON_CONTAINS operator requires String value");
        }
        break;
    }
  }
}
