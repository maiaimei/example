package org.example.repository;

import com.github.pagehelper.Page;
import java.util.List;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.example.mybatis.SQLProvider;
import org.example.mybatis.model.Filterable;

public interface BasicRepository<T> {

  @InsertProvider(type = SQLProvider.class, method = "insert")
  void insert(T domain);

  @UpdateProvider(type = SQLProvider.class, method = "update")
  void update(T domain);

  @DeleteProvider(type = SQLProvider.class, method = "delete")
  void delete(T domain);

  @SelectProvider(type = SQLProvider.class, method = "select")
  T select(T domain);

  @SelectProvider(type = SQLProvider.class, method = "selectByConditions")
  List<T> selectByConditions(T domain, Filterable filterable);

  @SelectProvider(type = SQLProvider.class, method = "selectByConditions")
  Page<T> selectByConditionsWithPage(T domain, Filterable filterable);

  void batchInsert(List<T> domains);

  void batchUpdate(List<T> domains);
}
