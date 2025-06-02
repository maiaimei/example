package org.example.mybatis.query.filter;

import java.util.List;
import java.util.Objects;
import org.example.mybatis.query.Queryable;
import org.example.mybatis.query.condition.Condition;
import org.example.mybatis.query.condition.SimpleCondition;
import org.example.mybatis.query.operator.SQLOperator;
import org.example.mybatis.query.operator.SQLOperator2;

/**
 * 过滤能力接口
 */
public interface Filterable extends Queryable {

  List<Condition> getConditions2();

  default SimpleCondition newSimpleCondition(String field, SQLOperator2 operator, Object value) {
    SimpleCondition simpleCondition = null;
    if (Objects.nonNull(value)) {
      simpleCondition = new SimpleCondition(field, operator, value);
    }
    return simpleCondition;
  }

  default SimpleCondition newSimpleCondition(String field, SQLOperator2 operator, Object firstValue, Object secondValue) {
    SimpleCondition simpleCondition = null;
    if (Objects.nonNull(firstValue) && Objects.nonNull(secondValue)) {
      simpleCondition = new SimpleCondition(field, operator, firstValue, secondValue);
    }
    return simpleCondition;
  }

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
