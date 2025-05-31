package org.example.mybatis.query.sort;

import java.util.List;
import org.example.mybatis.query.Queryable;

/**
 * 排序能力接口
 */
public interface Sortable extends Queryable {

  List<SortableItem> getSorting();
}
