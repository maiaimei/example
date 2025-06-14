package org.example.model;

import lombok.Data;

/**
 * Sorting criteria
 * 排序条件
 */
@Data
public class SortCriteria {

  /**
   * 排序字段名称
   */
  private String name;

  /**
   * 排序方向（"asc"升序，"desc"降序）
   */
  private String direction;
}
