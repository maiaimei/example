package cn.maiaimei.model;

import lombok.Data;

/**
 * Pagination information
 * 分页参数
 */
@Data
public class PageCriteria {

  /**
   * 当前页码（从1开始）
   */
  private Integer current;

  /**
   * 每页显示记录数
   */
  private Integer size;
}
