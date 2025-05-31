package org.example.mybatis.query.filter;

import java.util.List;
import java.util.Objects;
import org.example.mybatis.query.Queryable;

/**
 * 过滤能力接口
 */
public interface Filterable extends Queryable {

  List<FilterableItem> getConditions();

  default void addCondition(List<FilterableItem> conditions, String column, SQLOperator operator, Object value) {
    if (Objects.nonNull(value)) {
      conditions.add(new FilterableItem(column, operator, value));
    }
  }

  default void addBetweenCondition(List<FilterableItem> conditions, String column, Object firstValue, Object secondValue) {
    if (Objects.nonNull(firstValue) && Objects.nonNull(secondValue)) {
      conditions.add(new FilterableItem(column, SQLOperator.BETWEEN, firstValue, secondValue));
    }
  }

}
