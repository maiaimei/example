package org.example.mybatis.query.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;
import org.example.mybatis.query.operator.LogicalOperator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 条件组（支持AND/OR）
 */
@Data
public class ConditionGroup implements Condition {

  private LogicalOperator operator;
  private List<Condition> conditions;

  public ConditionGroup(LogicalOperator operator) {
    this.operator = operator;
    this.conditions = new ArrayList<>();
  }

  public void add(Condition condition) {
    conditions.add(condition);
  }

  @Override
  public String build(String dataSourceType, AtomicInteger conditionIndex) {
    if (CollectionUtils.isEmpty(conditions)) {
      return null;
    }

    List<String> sqlConditions = new ArrayList<>();
    for (Condition condition : conditions) {
      String sql = condition.build(dataSourceType, conditionIndex);
      if (StringUtils.hasText(sql)) {
        sqlConditions.add(sql);
      }
    }

    if (CollectionUtils.isEmpty(sqlConditions)) {
      return null;
    }

    return "(%s)".formatted(String.join(" %s ".formatted(operator.name()), sqlConditions));
  }
}