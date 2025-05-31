package org.example.mybatis.model;

import java.util.List;
import java.util.Objects;
import org.example.mybatis.SQLOperator;

public interface Filterable {

  List<FilterableItem> getConditions();

  default void addCondition(List<FilterableItem> conditions, String column, SQLOperator operator, Object value) {
    if (Objects.nonNull(value)) {
      conditions.add(new FilterableItem(column, operator, value));
    }
  }

  default void getBetweenCondition(List<FilterableItem> conditions, String column, Object firstValue, Object secondValue) {
    if (Objects.nonNull(firstValue) && Objects.nonNull(secondValue)) {
      conditions.add(new FilterableItem(column, SQLOperator.BETWEEN, firstValue, secondValue));
    }
  }

}
