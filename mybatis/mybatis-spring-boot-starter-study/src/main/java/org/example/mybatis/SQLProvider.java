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
import org.example.mybatis.query.Queryable;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.filter.ConditionGroup;
import org.example.mybatis.query.filter.Filterable;
import org.example.mybatis.query.filter.SimpleCondition;
import org.example.mybatis.query.select.FieldSelectable;
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

//  public String advancedSelect(Map<String, Object> params) {
//    Object domain = getParam(params, "domain");
//    Queryable queryable = getParam(params, "queryable");
//    validateDomain(domain);
//
//    final String tableName = getTableName(domain.getClass());
//    final List<String> selectFields = resolveSelectFields(queryable);
//    final List<Condition> conditions = resolveConditions(queryable);
//    final List<SortableItem> sorting = resolveSorting(queryable);
//
//    final SQLBuilder builder = SQLBuilder.builder()
//        .selectColumns(tableName, selectFields)
//        .whereByConditions(conditions)
//        .orderBy(sorting);
//
//    mergeParameters(params, builder.getParameters());
//    return builder.build();
//  }

  public String advancedSelect2(@Param("domain") Object domain,
      @Param("conditions") List<Condition> conditions,
      @Param("simpleConditions") List<SimpleCondition> simpleConditions,
      @Param("fields") List<String> fields) {
    validateDomain(domain);

    final String tableName = getTableName(domain.getClass());
    final List<SortableItem> sorting = resolveSorting(domain);

    final SQLBuilder builder = SQLBuilder.builder()
        .selectColumns(tableName, fields);

    if (!CollectionUtils.isEmpty(conditions)) {
      AtomicInteger index = new AtomicInteger(0);
      for (final Condition condition : conditions) {
        builder.whereByCondition(condition, index);
        if (condition instanceof ConditionGroup conditionGroup) {
          simpleConditions.addAll(conditionGroup.getSimpleConditions());
        } else if (condition instanceof SimpleCondition simpleCondition) {
          simpleConditions.add(simpleCondition);
        }
      }
    }

    final String sql = builder.orderBy(sorting).build();
    if (log.isDebugEnabled()) {
      log.debug(sql);
    }
    return sql;
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

  // 字段选择
  private List<String> resolveSelectFields(Queryable queryable) {
    if (queryable instanceof FieldSelectable fieldSelectable) {
      return getSelectFields(fieldSelectable.getSelectFields());
    }
    return null;
  }

  private List<String> getSelectFields(List<String> selectFields) {
    if (!CollectionUtils.isEmpty(selectFields)) {
      return selectFields.stream()
          .map(SQLHelper::camelToUnderscore)
          .collect(Collectors.toList());
    }
    return null;
  }

  // 条件解析
  private List<Condition> resolveConditions(Queryable queryable) {
    if (queryable instanceof Filterable filterable) {
      return filterable.getConditions();
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

  // 参数合并
  private void mergeParameters(Map<String, Object> params, Map<String, Object> builderParams) {
    Map<String, Object> existingParams = getParam(params, "params");
    if (existingParams != null) {
      existingParams.putAll(builderParams);
    }
  }

  @SuppressWarnings("unchecked")
  private <T> T getParam(Map<String, Object> params, String key) {
    return (T) params.get(key);
  }

}
