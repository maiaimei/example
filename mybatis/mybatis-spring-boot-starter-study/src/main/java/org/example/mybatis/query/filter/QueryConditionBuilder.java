package org.example.mybatis.query.filter;

import java.util.*;
import lombok.Builder;
import lombok.Data;
import org.example.mybatis.query.operator.LogicalOperator;
import org.example.mybatis.query.operator.SQLOperator;

/**
 * 查询条件构建器
 */
@Data
@Builder
public class QueryConditionBuilder {

  private ConditionGroup rootGroup;

  public static QueryConditionBuilder create() {
    return QueryConditionBuilder.builder()
        .rootGroup(new ConditionGroup(LogicalOperator.AND))
        .build();
  }

  public QueryConditionBuilder or(Condition... conditions) {
    return addLogicalConditions(LogicalOperator.OR, conditions);
  }

  public QueryConditionBuilder and(Condition... conditions) {
    return addLogicalConditions(LogicalOperator.AND, conditions);
  }

  public QueryConditionBuilder andEquals(String field, Object value) {
    return addConditionInternal(field, SQLOperator.EQ, value, null);
  }

  public QueryConditionBuilder andBetween(String field, Object value, Object secondValue) {
    return addConditionInternal(field, SQLOperator.BETWEEN, value, secondValue);
  }

  public QueryConditionBuilder andLike(String field, String value) {
    return addConditionInternal(field, SQLOperator.LIKE, value, null);
  }

  public QueryConditionBuilder andIlike(String field, String value) {
    return addConditionInternal(field, SQLOperator.LIKE_CASE_INSENSITIVE, value, null);
  }

  public QueryConditionBuilder andStartsWith(String field, String value) {
    return addConditionInternal(field, SQLOperator.STARTS_WITH, value, null);
  }

  public QueryConditionBuilder andEndsWith(String field, String value) {
    return addConditionInternal(field, SQLOperator.ENDS_WITH, value, null);
  }

  public QueryConditionBuilder andIn(String field, Object value) {
    return addConditionInternal(field, SQLOperator.IN, value, null);
  }

  public QueryConditionBuilder andJsonTextEquals(String field, String value, String jsonPath) {
    return addConditionInternal(field, SQLOperator.JSONB_TEXT_EQUALS, value, Map.of("jsonPath", jsonPath));
  }

  public QueryConditionBuilder andJsonContains(String field, String value) {
    return addConditionInternal(field, SQLOperator.JSON_CONTAINS, value, null);
  }

  public QueryConditionBuilder andWhere(String field, SQLOperator operator, Object value) {
    return addConditionInternal(field, operator, value, null);
  }

  public QueryConditionBuilder andWhere(String field, SQLOperator operator, Object value, Object secondValue) {
    return addConditionInternal(field, operator, value, secondValue);
  }

  public List<Condition> build() {
    return rootGroup.getConditions();
  }

  private QueryConditionBuilder addLogicalConditions(LogicalOperator operator, Condition... conditions) {
    if (Objects.isNull(conditions) || conditions.length == 0) {
      return this;
    }

    ConditionGroup group = new ConditionGroup(operator);
    Arrays.stream(conditions)
        .filter(Objects::nonNull)
        .forEach(group::add);

    if (!group.getConditions().isEmpty()) {
      rootGroup.add(group);
    }
    return this;
  }

  private QueryConditionBuilder addConditionInternal(String field, SQLOperator operator, Object firstValue, Object secondValue) {
    if (Objects.isNull(firstValue) || (operator == SQLOperator.BETWEEN && Objects.isNull(secondValue))) {
      return this;
    }

    SimpleCondition condition = new SimpleCondition(field, operator, firstValue, secondValue);
    rootGroup.add(condition);
    return this;
  }
}