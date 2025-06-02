package org.example.mybatis.query.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.example.mybatis.query.operator.LogicalOperator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 条件组（支持AND/OR）
 */
@Data
public class ConditionGroup implements Condition {

  private LogicalOperator operator; // AND/OR
  private List<Condition> conditions = new ArrayList<>();

  public ConditionGroup(LogicalOperator operator) {
    this.operator = operator;
  }

  public void add(Condition condition) {
    conditions.add(condition);
  }

  @Override
  public String build(String dataSourceType, int index) {
    if (CollectionUtils.isEmpty(conditions)) {
      return null;
    }

    List<String> sqlConditions = new ArrayList<>();
    for (Condition condition : conditions) {
      String sql = condition.build(dataSourceType, index);
      if (StringUtils.hasText(sql)) {
        sqlConditions.add(sql);
      }
    }

    if (CollectionUtils.isEmpty(sqlConditions)) {
      return null;
    }

    return "(" + String.join(" " + operator.name() + " ", sqlConditions) + ")";
  }

  @Override
  public Map<String, Object> getParameters(int index) {
    Map<String, Object> params = new HashMap<>();
    for (Condition condition : conditions) {
      params.putAll(condition.getParameters(index));
    }
    return params;
  }
}