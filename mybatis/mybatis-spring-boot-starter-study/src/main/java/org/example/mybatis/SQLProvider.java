package org.example.mybatis;

import static org.example.mybatis.SQLHelper.getFields;
import static org.example.mybatis.SQLHelper.getNotNullFieldMetadataList;
import static org.example.mybatis.SQLHelper.getTableName;
import static org.example.mybatis.SQLHelper.validateDomain;
import static org.example.mybatis.SQLHelper.validateDomainField;
import static org.example.mybatis.SQLHelper.validateDomains;

import java.lang.reflect.Field;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.example.model.PageCriteria;
import org.example.model.SortCriteria;
import org.example.mybatis.model.FieldMetadata;
import org.example.mybatis.query.filter.Condition;

@Slf4j
public class SQLProvider {

  public String insert(Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    final List<FieldMetadata> fieldMetadataList = getNotNullFieldMetadataList(domain);
    validateDomainField(fieldMetadataList);
    return SQLBuilder.buildInsertQuery(tableName, fieldMetadataList);
  }

  public String update(Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    final List<FieldMetadata> fieldMetadataList = getNotNullFieldMetadataList(domain);
    validateDomainField(fieldMetadataList);
    return SQLBuilder.buildUpdateQueryByPrimaryKey(tableName, fieldMetadataList);
  }

  public String delete(Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    return SQLBuilder.buildDeleteQueryByPrimaryKey(tableName);
  }

  public String select(Object domain) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    final List<FieldMetadata> fieldMetadataList = getNotNullFieldMetadataList(domain);
    return SQLBuilder.buildSelectQuery(tableName, fieldMetadataList);
  }

  public String advancedSelect(@Param("domain") Object domain,
      @Param("conditions") List<Condition> conditions,
      @Param("sorts") List<SortCriteria> sorts,
      @Param("fields") List<String> fields,
      @Param("page") PageCriteria page) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    return SQLBuilder.buildAdvancedSelectQuery(tableName, fields, conditions, sorts, page);
  }

  public String advancedCount(@Param("domain") Object domain,
      @Param("conditions") List<Condition> conditions) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    return SQLBuilder.buildCountQueryWithConditions(tableName, conditions);
  }

  public String advancedDelete(@Param("domain") Object domain,
      @Param("conditions") List<Condition> conditions) {
    validateDomain(domain);
    final String tableName = getTableName(domain.getClass());
    return SQLBuilder.buildDeleteQueryWithConditions(tableName, conditions);
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
    return SQLBuilder.buildDatabaseSpecificBatchInsert(tableName, fields, domains);
  }

  public String batchUpdate(@Param("domains") List<Object> domains) {
    validateDomains(domains);

    // TODO: batchUpdate
    return null;
  }
}
