package org.example.mybatis;

import static org.example.mybatis.SQLHelper.getNotNullFieldValues;
import static org.example.mybatis.SQLHelper.getTableName;
import static org.example.mybatis.SQLHelper.validateDomain;
import static org.example.mybatis.SQLHelper.validateDomainField;
import static org.example.mybatis.SQLHelper.validateDomains;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
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
    return SQLBuilder.builder().insert(tableName, notNullFieldValues).build();
  }

  public String update(Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    final List<FieldValue> notNullFieldValues = getNotNullFieldValues(domain);
    validateDomainField(notNullFieldValues);
    return SQLBuilder.builder().update(tableName, notNullFieldValues).build();
  }

  public String delete(Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    return SQLBuilder.builder().delete(tableName).build();
  }

  public String select(Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    final List<FieldValue> notNullFieldValues = getNotNullFieldValues(domain);
    return SQLBuilder.builder()
        .selectAllColumns(tableName)
        .whereByFieldValues(notNullFieldValues)
        .build();
  }

  public String advancedSelect(@Param("domain") Object domain,
      @Param("conditions") List<Condition> conditions,
      @Param("fields") List<String> fields) {
    validateDomain(domain);

    final String tableName = getTableName(domain.getClass());
    final List<SortableItem> sorting = resolveSorting(domain);

    AtomicInteger index = new AtomicInteger(0);
    return SQLBuilder.builder()
        .selectColumns(tableName, resolveColumns(fields))
        .whereByConditions(conditions, index)
        .orderBy(sorting).build();
  }

  public String batchInsert(Map<String, Object> params) {
    List<?> domains = (List<?>) params.get("domains");
    validateDomains(domains);

    // TODO: batchInsert
    return null;
  }

  public String batchUpdate(Map<String, Object> params) {
    List<?> domains = (List<?>) params.get("domains");
    validateDomains(domains);

    // TODO: batchUpdate
    return null;
  }

  private List<String> resolveColumns(List<String> selectFields) {
    if (!CollectionUtils.isEmpty(selectFields)) {
      return selectFields.stream()
          .map(SQLHelper::camelToUnderscore)
          .collect(Collectors.toList());
    }
    return null;
  }

  // 排序解析
  private List<SortableItem> resolveSorting(Object object) {
    if (object instanceof Sortable sortable) {
      return sortable.getSorting().stream()
          .peek(item -> item.setField(SQLHelper.camelToUnderscore(item.getField())))
          .collect(Collectors.toList());
    }
    return null;
  }

}
