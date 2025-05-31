package org.example.mybatis;

import static org.example.mybatis.SQLHelper.camelToUnderscore;
import static org.example.mybatis.SQLHelper.getNotNullFieldValues;
import static org.example.mybatis.SQLHelper.getTableName;
import static org.example.mybatis.SQLHelper.validateDomain;
import static org.example.mybatis.SQLHelper.validateDomainField;

import java.util.List;
import org.example.mybatis.model.FieldValue;
import org.example.mybatis.model.Filterable;
import org.example.mybatis.model.FilterableItem;
import org.example.mybatis.model.Queryable;
import org.example.mybatis.model.Sortable;
import org.example.mybatis.model.SortableItem;

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
        .select(tableName, notNullFieldValues)
        .build();
  }

  public String selectByConditions(Object domain, Queryable queryable) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    return SQLBuilder.builder()
        .select(tableName)
        .where(getConditions(queryable))
        .orderBy(getSorting(queryable))
        .build();
  }

  private List<FilterableItem> getConditions(Queryable queryable) {
    if (queryable instanceof Filterable filterable) {
      final List<FilterableItem> conditions = filterable.getConditions();
      for (FilterableItem condition : conditions) {
        condition.setColumn(camelToUnderscore(condition.getColumn()));
      }
      return conditions;
    }
    return null;
  }

  private List<SortableItem> getSorting(Queryable queryable) {
    if (queryable instanceof Sortable sortable) {
      final List<SortableItem> sorting = sortable.getSorting();
      for (SortableItem sortableItem : sorting) {
        sortableItem.setField(camelToUnderscore(sortableItem.getField()));
      }
      return sorting;
    }
    return null;
  }

}
