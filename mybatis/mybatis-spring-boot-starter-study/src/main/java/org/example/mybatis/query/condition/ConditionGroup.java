package org.example.mybatis.query.condition;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.example.mybatis.query.operator.LogicalOperator;

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
    if (conditions.isEmpty()) {
      return null;
    }

    List<String> sqlConditions = new ArrayList<>();
    for (Condition condition : conditions) {
      String sql = condition.build(dataSourceType, index++);
      if (sql != null) {
        sqlConditions.add(sql);
      }
    }

    if (sqlConditions.isEmpty()) {
      return null;
    }

    return "(" + String.join(" " + operator.name() + " ", sqlConditions) + ")";
  }
}