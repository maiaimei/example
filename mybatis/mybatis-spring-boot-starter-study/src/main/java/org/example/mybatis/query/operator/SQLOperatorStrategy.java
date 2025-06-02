package org.example.mybatis.query.operator;

/**
 * SQL 操作符策略接口
 */
public interface SQLOperatorStrategy {

  String buildCondition(String column, int index);
}