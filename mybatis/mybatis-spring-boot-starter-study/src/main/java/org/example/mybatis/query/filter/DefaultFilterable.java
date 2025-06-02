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

  public QueryConditionBuilder and(Condition... conditions) {
    builder.and(conditions);
    return builder;
  }

  public QueryConditionBuilder or(Condition... conditions) {
    builder.or(conditions);
    return builder;
  }

  public List<Condition> getConditions() {
    return builder.build();
  }

}
