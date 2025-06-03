package org.example.mybatis.query.filter;

import java.util.Objects;
import org.example.mybatis.query.operator.SQLOperator;

public class SimpleConditionFactory {

  public static SimpleCondition where(String field, SQLOperator operator, Object value) {
    return newSimpleCondition(field, operator, value, null);
  }

  public static SimpleCondition where(String field, SQLOperator operator, Object value, Object secondValue) {
    return newSimpleCondition(field, operator, value, secondValue);
  }

  // 相等条件查询
  public static SimpleCondition eq(String field, Object value) {
    return newSimpleCondition(field, SQLOperator.EQ, value, null);
  }

  // 模糊匹配条件
  public static SimpleCondition like(String field, String value) {
    return newSimpleCondition(field, SQLOperator.LIKE, value, null);
  }

  // 左模糊条件
  public static SimpleCondition startsWith(String field, String value) {
    return newSimpleCondition(field, SQLOperator.STARTS_WITH, value, null);
  }

  // 右模糊条件
  public static SimpleCondition endsWith(String field, String value) {
    return newSimpleCondition(field, SQLOperator.ENDS_WITH, value, null);
  }

  // 大小写不敏感模糊匹配条件
  public static SimpleCondition ilike(String field, String value) {
    return newSimpleCondition(field, SQLOperator.ILIKE, value, null);
  }

  // JSON 包含操作条件
  public static SimpleCondition jsonContains(String field, String value) {
    return newSimpleCondition(field, SQLOperator.JSON_CONTAINS, value, null);
  }

  private static SimpleCondition newSimpleCondition(String field, SQLOperator operator, Object firstValue, Object secondValue) {
    if (Objects.isNull(firstValue) || (operator == SQLOperator.BETWEEN && Objects.isNull(secondValue))) {
      return null;
    }
    return new SimpleCondition(field, operator, firstValue, secondValue);
  }
}
