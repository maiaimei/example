package org.example.mybatis.query.filter;

import java.util.List;

public class DefaultFilterable implements Filterable {

  private final QueryConditionBuilder builder;

  private DefaultFilterable() {
    this.builder = QueryConditionBuilder.create();
  }

  public static DefaultFilterable newInstance() {
    return new DefaultFilterable();
  }

  public QueryConditionBuilder and(SimpleCondition... conditions) {
    builder.and(conditions);
    return builder;
  }

  public QueryConditionBuilder or(SimpleCondition... conditions) {
    builder.or(conditions);
    return builder;
  }

  @Override
  public List<Condition> getConditions() {
    return builder.build();
  }

}
