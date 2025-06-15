package org.example.mybatis.query.filter;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;
import org.example.mybatis.SQLHelper;
import org.example.mybatis.query.operator.SQLOperator;
import org.example.mybatis.query.operator.SQLOperatorStrategy;
import org.example.mybatis.query.operator.SQLOperatorStrategyFactory;

/**
 * 简单条件
 */
@Data
public class SimpleCondition implements Condition {

  private String field;
  private SQLOperator operator;
  private Object value;
  private Map<String, Object> parameters;

  private int index;
  private String column;
  private String operatorToUse;

  public SimpleCondition(String field, SQLOperator operator, Object value, Map<String, Object> parameters) {
    this.field = field;
    this.operator = operator;
    this.value = value;
    this.parameters = parameters;
  }

  @Override
  public String build(AtomicInteger index) {
    this.index = index.getAndIncrement();
    this.column = SQLHelper.formatColumnName(field);
    this.operatorToUse = operator.getOperator();
    final SQLOperatorStrategy strategy = SQLOperatorStrategyFactory.getStrategy(operator);
    return strategy.buildCondition(this, this.index);
  }

}