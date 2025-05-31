package org.example.mybatis;

import static org.example.mybatis.SQLHelper.getNotNullFieldValues;
import static org.example.mybatis.SQLHelper.getTableName;
import static org.example.mybatis.SQLHelper.validateDomain;
import static org.example.mybatis.SQLHelper.validateDomainField;

import java.util.List;
import java.util.function.Consumer;
import org.apache.ibatis.jdbc.SQL;
import org.example.mybatis.model.Filterable;
import org.example.mybatis.model.FilterableItem;
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
        .whereConditions(buildWhereConditions(getConditions(domain)))
        .orderBy(getSorting(domain))
        .build();
  }

  public String selectByConditions(Object domain, Filterable filterable) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    return SQLBuilder.builder()
        .select(tableName)
        .whereConditions(buildWhereConditions(getConditions(domain)))
        .whereConditions(buildWhereConditions(filterable.getConditions()))
        .orderBy(getSorting(domain))
        .orderBy(getSorting(filterable))
        .build();
  }

  private List<FilterableItem> getConditions(Object object) {
    if (object instanceof Filterable filterable) {
      return filterable.getConditions();
    }
    return null;
  }

  private List<SortableItem> getSorting(Object object) {
    if (object instanceof Sortable sortable) {
      return sortable.getSorting();
    }
    return null;
  }

  private Consumer<SQL> buildWhereConditions(List<FilterableItem> conditions) {
    return sql -> {
      if (conditions == null || conditions.isEmpty()) {
        return;
      }

      for (int i = 0; i < conditions.size(); i++) {
        FilterableItem condition = conditions.get(i);
        String paramName = condition.getColumn() + i;

        switch (condition.getOperator()) {
          case EQ:
            sql.WHERE(String.format("%s = #{filterable.conditions[%d].value}",
                condition.getColumn(), i));
            break;
          case NE:
            sql.WHERE(String.format("%s <> #{filterable.conditions[%d].value}",
                condition.getColumn(), i));
            break;
          case LIKE:
            sql.WHERE(String.format("%s LIKE CONCAT('%%', #{filterable.conditions[%d].value}, '%%')",
                condition.getColumn(), i));
            break;
          case IN:
            sql.WHERE(String.format("%s IN " +
                "<foreach collection='filterable.conditions[%d].value' item='item' open='(' separator=',' close=')'>" +
                "#{item}" +
                "</foreach>", condition.getColumn(), i));
            break;
          case BETWEEN:
            sql.WHERE(String.format("%s BETWEEN #{filterable.conditions[%d].value} AND #{filterable.conditions[%d].secondValue}",
                condition.getColumn(), i, i));
            break;
          case IS_NULL:
            sql.WHERE(String.format("%s IS NULL", condition.getColumn()));
            break;
          case IS_NOT_NULL:
            sql.WHERE(String.format("%s IS NOT NULL", condition.getColumn()));
            break;
          // ... 其他操作符的处理
        }
      }
    };
  }
}
