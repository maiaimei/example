package cn.maiaimei.model;

import java.util.List;
import lombok.Data;

/**
 * The wrapper class for paging query results
 * 分页查询结果包装类
 *
 * @param <T> 结果数据类型
 */
@Data
public class PageableSearchResult<T> {

  /**
   * 当前页数据列表
   */
  private List<T> records;

  /**
   * 总记录数
   */
  private Long totalRecords;

  /**
   * 总页数
   */
  private Long totalPages;

  /**
   * 当前页码
   */
  private Integer currentPageNumber;

  /**
   * 每页显示记录数
   */
  private Integer pageSize;
}