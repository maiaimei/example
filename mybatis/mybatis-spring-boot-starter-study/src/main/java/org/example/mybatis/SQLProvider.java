package org.example.mybatis;

import static org.example.mybatis.SQLHelper.camelToUnderscore;
import static org.example.mybatis.SQLHelper.getNotNullFieldValues;
import static org.example.mybatis.SQLHelper.getTableName;
import static org.example.mybatis.SQLHelper.validateDomain;
import static org.example.mybatis.SQLHelper.validateDomainField;

import java.util.ArrayList;
import java.util.List;
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
        .selectAllColumnsWithConditions(tableName, notNullFieldValues)
        .build();
  }

  public String selectByConditions(Object domain, Queryable queryable) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    return SQLBuilder.builder()
        .selectSpecificColumns(tableName, getSelectFields(queryable))
        .where(getConditions(queryable))
        .orderBy(getSorting(queryable))
        .build();
  }

  private List<String> getSelectFields(Queryable queryable) {
    if (queryable instanceof FieldSelectable fieldSelectable) {
      final List<String> selectFields = fieldSelectable.getSelectFields();
      if (!CollectionUtils.isEmpty(selectFields)) {
        List<String> selectColumns = new ArrayList<>();
        for (String selectField : selectFields) {
          selectColumns.add(camelToUnderscore(selectField));
        }
        return selectColumns;
      }
    }
    return null;
  }

  private List<Condition> getConditions(Queryable queryable) {
    if (queryable instanceof Filterable filterable) {
      final List<Condition> conditions = filterable.getConditions();
      if (!CollectionUtils.isEmpty(conditions)) {
        return conditions;
      }
    }
    return null;
  }

  private List<SortableItem> getSorting(Queryable queryable) {
    if (queryable instanceof Sortable sortable) {
      final List<SortableItem> sorting = sortable.getSorting();
      if (!CollectionUtils.isEmpty(sorting)) {
        for (SortableItem sortableItem : sorting) {
          sortableItem.setField(camelToUnderscore(sortableItem.getField()));
        }
        return sorting;
      }
    }
    return null;
  }

}
