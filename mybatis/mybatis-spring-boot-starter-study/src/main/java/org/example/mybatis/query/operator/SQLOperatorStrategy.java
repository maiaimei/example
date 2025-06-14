package org.example.mybatis.query.operator;

import org.example.mybatis.query.filter.SimpleCondition;

/**
 * SQL 操作符策略接口
 */
public interface SQLOperatorStrategy {

  String buildCondition(SimpleCondition simpleCondition, int conditionIndex);
}