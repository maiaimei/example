package org.example.mybatis.model;

import java.util.List;

/**
 * 排序能力接口
 */
public interface Sortable extends Queryable {

  List<SortableItem> getSorting();
}
