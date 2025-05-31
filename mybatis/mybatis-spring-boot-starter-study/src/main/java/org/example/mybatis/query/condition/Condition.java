package org.example.mybatis.query.condition;

/**
 * 条件组合接口
 */
public interface Condition {

  String build(String dataSourceType, int index);
}