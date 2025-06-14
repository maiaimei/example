package org.example.model;

import java.util.List;
import lombok.Data;

/**
 * Query request basic class, including query criteria, sorting, and pagination information
 * 分页查询请求基础类，包含查询条件、排序和分页信息
 *
 * @param <T> 查询条件类型
 */
@Data
public class PageableSearchRequest<T> {

  /**
   * 查询条件
   */
  private T filter;

  /**
   * 排序条件列表
   */
  private List<SortCriteria> sort;

  /**
   * 分页参数
   */
  private PageCriteria page;
}