package org.example.mybatis.query.filter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    return addSimpleCondition(SimpleConditionFactory.eq(field, value));
  }

  public QueryConditionBuilder andLike(String field, String value) {
    return addSimpleCondition(SimpleConditionFactory.like(field, value));
  }

  public QueryConditionBuilder andIlike(String field, String value) {
    return addSimpleCondition(SimpleConditionFactory.ilike(field, value));
  }

  public QueryConditionBuilder andStartsWith(String field, String value) {
    return addSimpleCondition(SimpleConditionFactory.startsWith(field, value));
  }

  public QueryConditionBuilder andEndsWith(String field, String value) {
    return addSimpleCondition(SimpleConditionFactory.endsWith(field, value));
  }

  public QueryConditionBuilder andBetween(String field, Object startValue, Object endValue) {
    return addSimpleCondition(SimpleConditionFactory.between(field, startValue, endValue));
  }

  public QueryConditionBuilder andIn(String field, Object value) {
    return addSimpleCondition(SimpleConditionFactory.in(field, value));
  }

  public QueryConditionBuilder andJsonbTextEquals(String field, String value, String jsonPath) {
    return addSimpleCondition(SimpleConditionFactory.jsonbTextEquals(field, value, jsonPath));
  }

  public QueryConditionBuilder andJsonContains(String field, String value) {
    return addSimpleCondition(SimpleConditionFactory.jsonContains(field, value));
  }

  public QueryConditionBuilder andWhere(String field, SQLOperator operator, Object value) {
    return addSimpleCondition(SimpleConditionFactory.where(field, operator, value));
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

  private QueryConditionBuilder addSimpleCondition(SimpleCondition condition) {
    if (Objects.nonNull(condition)) {
      rootGroup.add(condition);
    }
    return this;
  }
}