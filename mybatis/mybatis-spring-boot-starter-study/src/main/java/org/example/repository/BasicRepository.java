package org.example.repository;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.example.mybatis.SQLProvider;
import org.example.mybatis.query.Queryable;

public interface BasicRepository<T> {

  @InsertProvider(type = SQLProvider.class, method = "insert")
  void insert(T domain);

  @UpdateProvider(type = SQLProvider.class, method = "update")
  void update(T domain);

  @DeleteProvider(type = SQLProvider.class, method = "delete")
  void delete(T domain);

  @SelectProvider(type = SQLProvider.class, method = "select")
  T select(T domain);

  @SelectProvider(type = SQLProvider.class, method = "advancedSelect")
  List<T> advancedSelect(@Param("domain") T domain,
      @Param("queryable") Queryable queryable,
      @Param("params") Map<String, Object> params);

  void batchInsert(List<T> domains);

  void batchUpdate(List<T> domains);
}
