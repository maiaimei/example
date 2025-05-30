package org.example.mybatis.query;

import lombok.Data;
import org.example.mybatis.SQLOperator;

// 定义查询条件类
@Data
public class QueryCondition {

  private String column;           // 列名
  private SQLOperator operator;    // 操作符
  private Object value;           // 值
  private Object secondValue;     // 用于BETWEEN操作符的第二个值

  public QueryCondition(String column, SQLOperator operator, Object value) {
    this.column = column;
    this.operator = operator;
    this.value = value;
  }

  public QueryCondition(String column, SQLOperator operator, Object value, Object secondValue) {
    this(column, operator, value);
    this.secondValue = secondValue;
  }
}
