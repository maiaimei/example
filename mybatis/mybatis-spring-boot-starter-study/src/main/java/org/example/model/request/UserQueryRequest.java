package org.example.model.request;

import java.util.List;
import lombok.Data;
import org.example.mybatis.query.page.Pageable;
import org.example.mybatis.query.sort.Sortable;
import org.example.mybatis.query.sort.SortableItem;

@Data
public class UserQueryRequest implements Sortable, Pageable {

  // 业务查询参数
  private String username;
  private String firstName;
  private String lastName;
  private Boolean isEnabled;
  private Boolean isDeleted;

  // 排序字段
  private List<SortableItem> sorting;

  // 分页字段
  private Integer current;
  private Integer size;
}
