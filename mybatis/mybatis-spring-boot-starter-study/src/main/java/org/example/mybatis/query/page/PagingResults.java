package org.example.mybatis.query.page;

import java.util.List;

/**
 * 分页结果
 */
public class PagingResults<T> {

  private List<T> records;
  private Long totalRecords;
  private Long totalPages;
  private Integer currentPageNumber;
  private Integer pageSize;
}
