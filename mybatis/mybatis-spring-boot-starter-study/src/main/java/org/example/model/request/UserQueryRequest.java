package org.example.model.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.example.mybatis.query.condition.Condition;
import org.example.mybatis.query.condition.QueryConditionBuilder;
import org.example.mybatis.query.filter.Filterable;
import org.example.mybatis.query.filter.FilterableItem;
import org.example.mybatis.query.operator.SQLOperator;
import org.example.mybatis.query.operator.SQLOperator2;
import org.example.mybatis.query.page.Pageable;
import org.example.mybatis.query.select.FieldSelectable;
import org.example.mybatis.query.sort.Sortable;
import org.example.mybatis.query.sort.SortableItem;

@Data
public class UserQueryRequest implements Filterable, Sortable, Pageable, FieldSelectable {

  // 业务查询参数
  private String username;
  private Boolean isEnabled;
  private Boolean isDeleted;

  // 查询字段
  private List<String> selectFields;

  // 排序参数
  private List<SortableItem> sorting;

  // 分页参数
  private Integer current;
  private Integer size;

  // 过滤条件
  public List<FilterableItem> getConditions() {
    List<FilterableItem> conditions = new ArrayList<>();
    addCondition(conditions, "username", SQLOperator.LIKE, username);
    addCondition(conditions, "isEnabled", SQLOperator.EQ, isEnabled);
    addCondition(conditions, "isDeleted", SQLOperator.EQ, isDeleted);
    return conditions;
  }

  @Override
  public List<Condition> getConditions2() {
    QueryConditionBuilder builder = QueryConditionBuilder.create();
    builder.and(newSimpleCondition("username", SQLOperator2.LIKE, username));
    builder.and(newSimpleCondition("isEnabled", SQLOperator2.EQ, isEnabled));
    builder.and(newSimpleCondition("isDeleted", SQLOperator2.EQ, isDeleted));
    return builder.build();
  }
}
