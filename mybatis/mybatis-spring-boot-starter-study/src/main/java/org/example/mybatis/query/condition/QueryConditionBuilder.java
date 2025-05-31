package org.example.mybatis.query.condition;

import java.util.Arrays;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.example.mybatis.query.operator.LogicalOperator;

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
    ConditionGroup group = new ConditionGroup(LogicalOperator.AND);
    Arrays.stream(conditions).forEach(group::add);
    rootGroup.add(group);
    return this;
  }

  public QueryConditionBuilder or(Condition... conditions) {
    ConditionGroup group = new ConditionGroup(LogicalOperator.OR);
    Arrays.stream(conditions).forEach(group::add);
    rootGroup.add(group);
    return this;
  }

  public List<Condition> build() {
    return rootGroup.getConditions();
  }
}