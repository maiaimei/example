package org.example.model.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.example.mybatis.query.filter.Filterable;
import org.example.mybatis.query.filter.FilterableItem;
import org.example.mybatis.query.filter.SQLOperator;
import org.example.mybatis.query.page.Pageable;
import org.example.mybatis.query.sort.Sortable;
import org.example.mybatis.query.sort.SortableItem;

@Data
public class UserQueryRequest implements Filterable, Sortable, Pageable {

  // 业务查询参数
  private String username;

  // 过滤条件
  private List<FilterableItem> conditions = new ArrayList<>();

  // 排序参数
  private List<SortableItem> sorting;

  // 分页参数
  private Integer current;
  private Integer size;

  /**
   * 构建查询条件
   */
  public void buildConditions() {
    addCondition(conditions, "username", SQLOperator.LIKE, username);
  }
}
