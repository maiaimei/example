package org.example.model.request;

import java.util.List;
import lombok.Data;
import org.example.mybatis.SQLOperator;
import org.example.mybatis.model.Filterable;
import org.example.mybatis.model.FilterableItem;
import org.example.mybatis.model.Sortable;
import org.example.mybatis.model.SortableItem;

@Data
public class UserQueryRequest implements Filterable, Sortable {

  private String username;
  private List<SortableItem> sorting;

  @Override
  public List<FilterableItem> getConditions() {
    return List.of(
        getCondition("username", SQLOperator.LIKE, username)
    );
  }
}
