package org.example.mybatis.query.filter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 条件组合接口
 */
public interface Condition {

  String build(String dataSourceType, AtomicInteger conditionIndex);
}