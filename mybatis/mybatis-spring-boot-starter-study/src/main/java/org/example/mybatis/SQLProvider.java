package org.example.mybatis;

import static org.example.mybatis.SQLHelper.getNotNullFieldValues;
import static org.example.mybatis.SQLHelper.getTableName;
import static org.example.mybatis.SQLHelper.validateDomain;
import static org.example.mybatis.SQLHelper.validateDomainField;

import java.util.List;

public class SQLProvider {

  public String insert(final Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    final List<FieldValue> notNullFieldValues = getNotNullFieldValues(domain);
    validateDomainField(notNullFieldValues);
    return SQLBuilder.builder().insert(tableName, notNullFieldValues).build();
  }

  public String update(final Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    final List<FieldValue> notNullFieldValues = getNotNullFieldValues(domain);
    validateDomainField(notNullFieldValues);
    return SQLBuilder.builder().update(tableName, notNullFieldValues).build();
  }

  public String delete(final Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    return SQLBuilder.builder().delete(tableName).build();
  }

  public String select(final Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    final List<FieldValue> notNullFieldValues = getNotNullFieldValues(domain);
    return SQLBuilder.builder().select(tableName, notNullFieldValues).build();
  }
}
