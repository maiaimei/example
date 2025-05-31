package org.example.mybatis.model;

/**
 * 分页能力接口
 */
public interface Pageable extends Queryable {

  Integer getCurrent();

  Integer getSize();
}
