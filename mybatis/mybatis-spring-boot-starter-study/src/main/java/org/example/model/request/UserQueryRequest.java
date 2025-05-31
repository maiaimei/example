package org.example.model.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.example.mybatis.query.filter.Filterable;
import org.example.mybatis.query.filter.FilterableItem;
import org.example.mybatis.query.filter.SQLOperator;
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
}
