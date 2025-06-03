package org.example.mybatis;

import static org.example.mybatis.SQLHelper.getFields;
import static org.example.mybatis.SQLHelper.getNotNullFieldValues;
import static org.example.mybatis.SQLHelper.getTableName;
import static org.example.mybatis.SQLHelper.validateDomain;
import static org.example.mybatis.SQLHelper.validateDomainField;
import static org.example.mybatis.SQLHelper.validateDomains;

import java.lang.reflect.Field;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.example.mybatis.model.FieldValue;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.sort.Sortable;
import org.example.mybatis.query.sort.SortableItem;
import org.springframework.util.CollectionUtils;

@Slf4j
public class SQLProvider {

  public String insert(Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    final List<FieldValue> notNullFieldValues = getNotNullFieldValues(domain);
    validateDomainField(notNullFieldValues);
    return SQLBuilder.builder().buildInsertQuery(tableName, notNullFieldValues).build();
  }

  public String update(Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    final List<FieldValue> notNullFieldValues = getNotNullFieldValues(domain);
    validateDomainField(notNullFieldValues);
    return SQLBuilder.builder().buildUpdateQuery(tableName, notNullFieldValues).build();
  }

  public String delete(Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    return SQLBuilder.builder().buildDeleteQuery(tableName).buildWhereClauseWithPrimaryKey().build();
  }

  public String select(Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    final List<FieldValue> notNullFieldValues = getNotNullFieldValues(domain);
    return SQLBuilder.builder()
        .buildSelectQueryWithAllColumns(tableName)
        .buildWhereClauseWithFields(notNullFieldValues)
        .build();
  }

  public String advancedSelect(@Param("domain") Object domain,
      @Param("conditions") List<Condition> conditions,
      @Param("sorting") List<SortableItem> sorting,
      @Param("fields") List<String> fields) {
    validateDomain(domain);

    final String tableName = getTableName(domain.getClass());

    return SQLBuilder.builder()
        .buildSelectQueryWithColumns(tableName, fields)
        .buildWhereClauseWithConditions(conditions)
        .buildOrderByClause(getSorting(domain, sorting))
        .build();
  }

  public String advancedCount(@Param("domain") Object domain,
      @Param("conditions") List<Condition> conditions) {
    validateDomain(domain);

    final String tableName = getTableName(domain.getClass());

    return SQLBuilder.builder()
        .buildCountQuery(tableName)
        .buildWhereClauseWithConditions(conditions)
        .build();
  }

  public String advancedDelete(@Param("domain") Object domain,
      @Param("conditions") List<Condition> conditions) {
    validateDomain(domain);

    final String tableName = getTableName(domain.getClass());

    return SQLBuilder.builder()
        .buildDeleteQuery(tableName)
        .buildWhereClauseWithConditions(conditions)
        .build();
  }

  public String batchInsert(@Param("domains") List<Object> domains) {
    // 验证输入的 domains 列表是否为空
    validateDomains(domains);

    // 获取第一个 domain 的表名（假设所有 domain 属于同一个表）
    final Object firstDomain = domains.get(0);
    final Class<?> firstDomainClass = firstDomain.getClass();
    final String tableName = getTableName(firstDomainClass);
    final List<Field> fields = getFields(firstDomainClass);

    // 使用 SQLBuilder 构建批量插入 SQL
    return SQLBuilder.newInstance().buildBatchInsertQuery(tableName, fields, domains);
  }

  public String batchUpdate(@Param("domains") List<Object> domains) {
    validateDomains(domains);

    // TODO: batchUpdate
    return null;
  }

  private List<SortableItem> getSorting(Object object, List<SortableItem> sorting) {
    if (CollectionUtils.isEmpty(sorting) && object instanceof Sortable sortable) {
      return sortable.getSorting();
    }
    return sorting;
  }

}
