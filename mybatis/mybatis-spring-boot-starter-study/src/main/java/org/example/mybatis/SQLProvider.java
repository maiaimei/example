package org.example.mybatis;

import static org.example.mybatis.SQLHelper.getNotNullFieldValues;
import static org.example.mybatis.SQLHelper.getTableName;
import static org.example.mybatis.SQLHelper.validateDomain;
import static org.example.mybatis.SQLHelper.validateDomainField;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.example.mybatis.model.FieldValue;
import org.example.mybatis.query.Queryable;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.filter.Filterable;
import org.example.mybatis.query.select.FieldSelectable;
import org.example.mybatis.query.sort.Sortable;
import org.example.mybatis.query.sort.SortableItem;
import org.springframework.util.CollectionUtils;

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

  public String advancedSelect(Map<String, Object> params) {
    Object domain = params.get("domain");
    Queryable queryable = (Queryable) params.get("queryable");
    validateDomain(domain);

    final String tableName = getTableName(domain.getClass());
    final List<String> selectFields = resolveSelectFields(queryable);
    final List<Condition> conditions = resolveConditions(queryable);
    final List<SortableItem> sorting = resolveSorting(queryable);

    final SQLBuilder builder = SQLBuilder.builder()
        .selectColumns(tableName, selectFields)
        .whereByConditions(conditions)
        .orderBy(sorting);

    mergeParameters(params, builder.getParameters());
    return builder.build();
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
  private List<SortableItem> resolveSorting(Queryable queryable) {
    if (queryable instanceof Sortable sortable) {
      return sortable.getSorting().stream()
          .peek(item -> item.setField(SQLHelper.camelToUnderscore(item.getField())))
          .collect(Collectors.toList());
    }
    return null;
  }

  // 参数合并
  @SuppressWarnings("unchecked")
  private void mergeParameters(Map<String, Object> params, Map<String, Object> builderParams) {
    Map<String, Object> existingParams = (Map<String, Object>) params.get("params");
    if (existingParams != null) {
      existingParams.putAll(builderParams);
    }
  }

}
