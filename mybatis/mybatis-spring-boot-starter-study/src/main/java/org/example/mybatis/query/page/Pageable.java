package org.example.mybatis.query.page;

import org.example.mybatis.query.Queryable;

/**
 * 分页能力接口
 */
public interface Pageable extends Queryable {

  Integer getCurrentPageNumber();

  Integer getPageSize();
}
