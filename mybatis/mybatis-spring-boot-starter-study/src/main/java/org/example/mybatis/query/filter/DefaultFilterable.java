//package org.example.mybatis.query.filter;
//
//import java.util.ArrayList;
//import java.util.List;
//import lombok.Getter;
//import org.example.mybatis.query.operator.SQLOperator;
//
//@Getter
//public class DefaultFilterable implements Filterable {
//
//  private final List<FilterableItem> conditions = new ArrayList<>();
//
//  public void addCondition(String field, SQLOperator operator, Object value) {
//    addCondition(conditions, field, operator, value);
//  }
//
//  public void addBetweenCondition(String field, Object firstValue, Object secondValue) {
//    addBetweenCondition(conditions, field, firstValue, secondValue);
//  }
//}
