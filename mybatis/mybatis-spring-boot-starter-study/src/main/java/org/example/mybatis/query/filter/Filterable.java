package org.example.mybatis.query.filter;

import java.util.List;
import java.util.Objects;
import org.example.mybatis.query.Queryable;
import org.example.mybatis.query.operator.SQLOperator;

/**
 * 过滤能力接口
 */
public interface Filterable extends Queryable {

  List<Condition> getConditions();

  default SimpleCondition newSimpleCondition(String field, SQLOperator operator, Object value) {
    SimpleCondition simpleCondition = null;
    if (Objects.nonNull(value)) {
      simpleCondition = new SimpleCondition(field, operator, value);
    }
    return simpleCondition;
  }

  default SimpleCondition newSimpleCondition(String field, SQLOperator operator, Object firstValue, Object secondValue) {
    SimpleCondition simpleCondition = null;
    if (Objects.nonNull(firstValue) && Objects.nonNull(secondValue)) {
      simpleCondition = new SimpleCondition(field, operator, firstValue, secondValue);
    }
    return simpleCondition;
  }

}
