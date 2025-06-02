package org.example.mybatis.query.filter;

import java.util.Map;

/**
 * 条件组合接口
 */
public interface Condition {

  String build(String dataSourceType, int index);

  Map<String, Object> getParameters(int index);
}