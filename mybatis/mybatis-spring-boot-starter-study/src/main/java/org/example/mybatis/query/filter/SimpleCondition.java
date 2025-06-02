package org.example.mybatis.query.filter;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;
import org.example.mybatis.SQLHelper;
import org.example.mybatis.query.operator.SQLOperator;
import org.example.mybatis.query.operator.SQLOperatorStrategyFactory;

/**
 * 简单条件
 */
@Data
public class SimpleCondition implements Condition {

  private String field;
  private SQLOperator operator;
  private Object value;
  private Object secondValue; // 用于 BETWEEN 等操作符

  public SimpleCondition(String field, SQLOperator operator, Object value) {
    this.field = field;
    this.operator = operator;
    this.value = value;
  }

  public SimpleCondition(String field, SQLOperator operator, Object value, Object secondValue) {
    this(field, operator, value);
    this.secondValue = secondValue;
  }

  @Override
  public String build(AtomicInteger index) {
    final String column = SQLHelper.formatName(SQLHelper.camelToUnderscore(field));
    return SQLOperatorStrategyFactory.getStrategy(operator)
        .buildCondition(column, index.getAndIncrement());
  }

}