package org.example.mybatis.query.filter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Data;
import org.example.mybatis.query.operator.LogicalOperator;
import org.example.mybatis.query.operator.SQLOperator;
import org.springframework.util.CollectionUtils;

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

  public QueryConditionBuilder and(Condition... conditions) {
    addConditions(LogicalOperator.AND, conditions);
    return this;
  }

  public QueryConditionBuilder or(Condition... conditions) {
    addConditions(LogicalOperator.OR, conditions);
    return this;
  }

  public QueryConditionBuilder addCondition(String field, SQLOperator operator, Object value) {
    if (Objects.nonNull(value)) {
      SimpleCondition simpleCondition = new SimpleCondition(field, operator, value);
      rootGroup.add(simpleCondition);
    }
    return this;
  }

  public QueryConditionBuilder addCondition(String field, SQLOperator operator, Object firstValue, Object secondValue) {
    if (Objects.nonNull(firstValue) && Objects.nonNull(secondValue)) {
      SimpleCondition simpleCondition = new SimpleCondition(field, operator, firstValue, secondValue);
      rootGroup.add(simpleCondition);
    }
    return this;
  }

  public List<Condition> build() {
    return rootGroup.getConditions();
  }

  private void addConditions(LogicalOperator operator, Condition... conditions) {
    if (Objects.nonNull(conditions) && conditions.length > 0) {
      ConditionGroup group = new ConditionGroup(operator);
      Arrays.stream(conditions)
          .filter(Objects::nonNull)
          .forEach(group::add);

      if (!CollectionUtils.isEmpty(group.getConditions())) {
        rootGroup.add(group);
      }
    }
  }
}