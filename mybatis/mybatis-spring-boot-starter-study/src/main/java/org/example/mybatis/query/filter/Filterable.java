package org.example.mybatis.query.filter;

import java.util.List;
import java.util.Objects;
import org.example.mybatis.query.Queryable;
import org.example.mybatis.query.operator.SQLOperator;

/**
 * 过滤能力接口
 */
public interface Filterable extends Queryable {

  List<FilterableItem> getConditions();

  default void addCondition(List<FilterableItem> conditions, String field, SQLOperator operator, Object value) {
    if (Objects.nonNull(value)) {
      conditions.add(new FilterableItem(field, operator, value));
    }
  }

  default void addBetweenCondition(List<FilterableItem> conditions, String field, Object firstValue, Object secondValue) {
    if (Objects.nonNull(firstValue) && Objects.nonNull(secondValue)) {
      conditions.add(new FilterableItem(field, SQLOperator.BETWEEN, firstValue, secondValue));
    }
  }

}
