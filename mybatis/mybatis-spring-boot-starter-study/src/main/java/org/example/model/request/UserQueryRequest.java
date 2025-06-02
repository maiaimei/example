package org.example.model.request;

import java.util.List;
import lombok.Data;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.filter.Filterable;
import org.example.mybatis.query.filter.QueryConditionBuilder;
import org.example.mybatis.query.operator.SQLOperator;
import org.example.mybatis.query.page.Pageable;
import org.example.mybatis.query.select.FieldSelectable;
import org.example.mybatis.query.sort.Sortable;
import org.example.mybatis.query.sort.SortableItem;

@Data
public class UserQueryRequest implements FieldSelectable, Filterable, Sortable, Pageable {

  // 业务查询参数
  private String username;
  private Boolean isEnabled;
  private Boolean isDeleted;

  // 查询字段
  private List<String> selectFields;

  // 排序字段
  private List<SortableItem> sorting;

  // 分页字段
  private Integer current;
  private Integer size;

  // 构建过滤条件
  @Override
  public List<Condition> getConditions() {
    QueryConditionBuilder builder = QueryConditionBuilder.create();
    builder.and(
        newSimpleCondition("isEnabled", SQLOperator.EQ, isEnabled),
        newSimpleCondition("isDeleted", SQLOperator.EQ, isDeleted)
    ).or(
        newSimpleCondition("username", SQLOperator.LIKE, username),
        newSimpleCondition("username", SQLOperator.LIKE, "mai")
    );
    return builder.build();
  }
}
