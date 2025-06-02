package org.example.repository;

import java.util.List;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
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

  @SelectProvider(type = SQLProvider.class, method = "selectByQueryable")
  List<T> selectByQueryable(T domain, Queryable queryable);

  void batchInsert(List<T> domains);

  void batchUpdate(List<T> domains);
}
