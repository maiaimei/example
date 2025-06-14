package org.example.mybatis.repository;

import java.util.List;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.example.model.PageCriteria;
import org.example.model.SortCriteria;
import org.example.mybatis.SQLProvider;
import org.example.mybatis.query.filter.Condition;

public interface BaseRepository<T> {

  @InsertProvider(type = SQLProvider.class, method = "insert")
  void insert(T domain);

  @UpdateProvider(type = SQLProvider.class, method = "update")
  void update(T domain);

  @DeleteProvider(type = SQLProvider.class, method = "delete")
  void delete(T domain);

  @SelectProvider(type = SQLProvider.class, method = "select")
  List<T> select(T domain);

  @SelectProvider(type = SQLProvider.class, method = "advancedSelect")
  List<T> advancedSelect(@Param("domain") T domain,
      @Param("conditions") List<Condition> conditions,
      @Param("sorts") List<SortCriteria> sorts,
      @Param("fields") List<String> fields,
      @Param("page") PageCriteria page);

  @SelectProvider(type = SQLProvider.class, method = "advancedCount")
  long advancedCount(@Param("domain") T domain,
      @Param("conditions") List<Condition> conditions);

  @SelectProvider(type = SQLProvider.class, method = "advancedDelete")
  void advancedDelete(@Param("domain") T domain,
      @Param("conditions") List<Condition> conditions);

  @InsertProvider(type = SQLProvider.class, method = "batchInsert")
  void batchInsert(@Param("domains") List<T> domains);

  @UpdateProvider(type = SQLProvider.class, method = "batchUpdate")
  void batchUpdate(@Param("domains") List<T> domains);
}
