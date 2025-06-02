package org.example.mybatis.query.filter;

/**
 * 条件组合接口
 */
public interface Condition {

  String build(String dataSourceType, int index);
}