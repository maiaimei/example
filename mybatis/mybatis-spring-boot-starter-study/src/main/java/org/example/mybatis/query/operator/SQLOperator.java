package org.example.mybatis.query.operator;

/**
 * SQL操作符枚举
 */
public enum SQLOperator {
  EQ,          // 等于
  NE,          // 不等于
  GT,          // 大于
  GE,          // 大于等于
  LT,          // 小于
  LE,          // 小于等于
  LIKE,        // 模糊查询
  NOT_LIKE,    // NOT LIKE查询
  STARTS_WITH, // 左模糊
  ENDS_WITH,   // 右模糊
  IN,          // IN查询
  NOT_IN,      // NOT IN查询
  BETWEEN,     // 范围查询
  IS_NULL,     // 为空
  IS_NOT_NULL  // 不为空
}