package org.example.mybatis.model;

import java.util.ArrayList;
import java.util.List;
import org.example.mybatis.SQLOperator;

public class DefaultFilterable implements Filterable {

  private final List<FilterableItem> conditions;

  private DefaultFilterable() {
    this.conditions = new ArrayList<>();
  }

  public void addCondition(String column, SQLOperator operator, Object value) {
    addCondition(conditions, column, operator, value);
  }

  public void getBetweenCondition(List<FilterableItem> conditions, String column, Object firstValue, Object secondValue) {
    addBetweenCondition(conditions, column, firstValue, secondValue);
  }

  @Override
  public List<FilterableItem> getConditions() {
    return conditions;
  }

  public static DefaultFilterable newInstance() {
    return new DefaultFilterable();
  }
}
