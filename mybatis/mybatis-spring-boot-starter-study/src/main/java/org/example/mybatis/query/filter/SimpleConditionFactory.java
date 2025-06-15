package org.example.mybatis.query.filter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.example.mybatis.query.operator.SQLOperator;

public class SimpleConditionFactory {

  private static final String START_VALUE = "startValue";
  private static final String END_VALUE = "endValue";
  private static final String JSON_PATH = "jsonPath";
  private static final String NESTED_FIELD = "nestedField";

  public static SimpleCondition eq(String field, Object value) {
    return newSimpleCondition(field, SQLOperator.EQ, value);
  }

  public static SimpleCondition ne(String field, Object value) {
    return newSimpleCondition(field, SQLOperator.NE, value);
  }

  public static SimpleCondition gt(String field, Object value) {
    return newSimpleCondition(field, SQLOperator.GT, value);
  }

  public static SimpleCondition ge(String field, Object value) {
    return newSimpleCondition(field, SQLOperator.GE, value);
  }

  public static SimpleCondition lt(String field, Object value) {
    return newSimpleCondition(field, SQLOperator.LT, value);
  }

  public static SimpleCondition le(String field, Object value) {
    return newSimpleCondition(field, SQLOperator.LE, value);
  }

  public static SimpleCondition like(String field, String value) {
    return newSimpleCondition(field, SQLOperator.LIKE, value);
  }

  public static SimpleCondition notLike(String field, String value) {
    return newSimpleCondition(field, SQLOperator.NOT_LIKE, value);
  }

  public static SimpleCondition ilike(String field, String value) {
    return newSimpleCondition(field, SQLOperator.LIKE_CASE_INSENSITIVE, value);
  }

  public static SimpleCondition notIlike(String field, String value) {
    return newSimpleCondition(field, SQLOperator.NOT_LIKE_CASE_INSENSITIVE, value);
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

  public static SimpleCondition in(String field, List<?> value) {
    return newSimpleCondition(field, SQLOperator.IN, value);
  }

  public static SimpleCondition notIn(String field, List<?> value) {
    return newSimpleCondition(field, SQLOperator.NOT_IN, value);
  }

  public static SimpleCondition jsonbContains(String field, Object value) {
    return newSimpleCondition(field, SQLOperator.JSONB_CONTAINS, value);
  }

  public static SimpleCondition jsonbNestedContains(String field, String value, String jsonPath) {
    return newSimpleCondition(field, SQLOperator.JSONB_NESTED_CONTAINS, value, Map.of(JSON_PATH, jsonPath));
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

  public static SimpleCondition arrayContains(String field, Object value) {
    return newSimpleCondition(field, SQLOperator.ARRAY_CONTAINS, value);
  }

  private static SimpleCondition newSimpleCondition(String field, SQLOperator operator, Object value) {
    return newSimpleCondition(field, operator, value, null);
  }

  private static SimpleCondition newSimpleCondition(String field, SQLOperator operator, Object value,
      Map<String, Object> parameters) {
    if (Objects.isNull(value)) {
      return null;
    }
    return new SimpleCondition(field, operator, value, parameters);
  }
}
