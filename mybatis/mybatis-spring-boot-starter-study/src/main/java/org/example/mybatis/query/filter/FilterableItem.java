package org.example.mybatis.query.filter;

import lombok.Data;

// 定义查询条件类
@Data
public class FilterableItem {

  private String column;        // 列名
  private SQLOperator operator; // 操作符
  private Object value;         // 值
  private Object secondValue;   // 用于BETWEEN操作符的第二个值

  public FilterableItem(String column, SQLOperator operator, Object value) {
    this.column = column;
    this.operator = operator;
    this.value = value;
  }

  public FilterableItem(String column, SQLOperator operator, Object value, Object secondValue) {
    this(column, operator, value);
    this.secondValue = secondValue;
  }
}
