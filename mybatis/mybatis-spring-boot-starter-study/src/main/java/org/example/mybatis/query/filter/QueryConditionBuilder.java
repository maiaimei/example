package org.example.mybatis.query.filter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Data;
import org.example.mybatis.query.operator.LogicalOperator;
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
    if (Objects.nonNull(conditions) && conditions.length > 0) {
      ConditionGroup group = new ConditionGroup(LogicalOperator.AND);
      Arrays.stream(conditions)
          .filter(Objects::nonNull)
          .forEach(group::add);

      // 如果group中添加了条件，才添加到rootGroup
      if (!CollectionUtils.isEmpty(group.getConditions())) {
        rootGroup.add(group);
      }
    }
    return this;
  }

  public QueryConditionBuilder or(Condition... conditions) {
    if (Objects.nonNull(conditions) && conditions.length > 0) {
      ConditionGroup group = new ConditionGroup(LogicalOperator.OR);
      Arrays.stream(conditions)
          .filter(Objects::nonNull)
          .forEach(group::add);

      // 如果group中添加了条件，才添加到rootGroup
      if (!CollectionUtils.isEmpty(group.getConditions())) {
        rootGroup.add(group);
      }
    }
    return this;
  }

  public List<Condition> build() {
    return rootGroup.getConditions();
  }
}