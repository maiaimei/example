package org.example.repository;

import java.util.List;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.example.mybatis.SQLProvider;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.filter.SimpleCondition;

public interface BasicRepository<T> {

  @InsertProvider(type = SQLProvider.class, method = "insert")
  void insert(T domain);

  @UpdateProvider(type = SQLProvider.class, method = "update")
  void update(T domain);

  @DeleteProvider(type = SQLProvider.class, method = "delete")
  void delete(T domain);

  @SelectProvider(type = SQLProvider.class, method = "select")
  T select(T domain);

//  @SelectProvider(type = SQLProvider.class, method = "advancedSelect")
//  List<T> advancedSelect(@Param("domain") T domain,
//      @Param("queryable") Queryable queryable,
//      @Param("params") Map<String, Object> params);

  @SelectProvider(type = SQLProvider.class, method = "advancedSelect2")
  List<T> advancedSelect2(@Param("domain") T domain,
      @Param("conditions") List<Condition> conditions,
      @Param("simpleConditions") List<SimpleCondition> simpleConditions,
      @Param("fields") List<String> fields);

  @InsertProvider(type = SQLProvider.class, method = "batchInsert")
  void batchInsert(@Param("domains") List<T> domains);

  @UpdateProvider(type = SQLProvider.class, method = "batchUpdate")
  void batchUpdate(@Param("domains") List<T> domains);
}
