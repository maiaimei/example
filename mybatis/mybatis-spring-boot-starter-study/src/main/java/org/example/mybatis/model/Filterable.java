package org.example.mybatis.model;

import java.util.List;
import java.util.Objects;
import org.example.mybatis.SQLOperator;

public interface Filterable {

  List<FilterableItem> getConditions();

  default FilterableItem getCondition(String column, SQLOperator operator, Object value) {
    if (Objects.nonNull(value)) {
      return new FilterableItem(column, operator, value);
    }
    return null;
  }

  default FilterableItem getBetweenCondition(String column, Object firstValue, Object secondValue) {
    if (Objects.nonNull(firstValue) && Objects.nonNull(secondValue)) {
      return new FilterableItem(column, SQLOperator.BETWEEN, firstValue, secondValue);
    }
    return null;
  }

}
