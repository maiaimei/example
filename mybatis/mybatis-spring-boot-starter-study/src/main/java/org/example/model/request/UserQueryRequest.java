package org.example.model.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.example.mybatis.SQLOperator;
import org.example.mybatis.model.Filterable;
import org.example.mybatis.model.FilterableItem;
import org.example.mybatis.model.Pageable;
import org.example.mybatis.model.Sortable;
import org.example.mybatis.model.SortableItem;

@Data
public class UserQueryRequest implements Filterable, Sortable, Pageable {

  private String username;
  private List<SortableItem> sorting;
  private Integer current;
  private Integer size;

  @Override
  public List<FilterableItem> getConditions() {
    List<FilterableItem> conditions = new ArrayList<>();
    addCondition(conditions, "username", SQLOperator.LIKE, username);
    return conditions;
  }
}
