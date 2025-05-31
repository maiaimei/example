package org.example.mybatis.query.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.mybatis.SQLHelper;
import org.example.mybatis.query.operator.SQLOperator2;
import org.example.mybatis.query.operator.SQLOperatorStrategyFactory;

/**
 * 简单条件
 */
@Data
@AllArgsConstructor
public class SimpleCondition implements Condition {

  private String field;
  private SQLOperator2 operator;
  private Object value;
  private Object secondValue; // 用于 BETWEEN 等操作符

  @Override
  public String build(String dataSourceType, int index) {
    return SQLOperatorStrategyFactory.getStrategy(operator)
        .buildCondition(SQLHelper.formatName(dataSourceType, field), index);
  }
}