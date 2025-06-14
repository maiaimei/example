package org.example.mybatis.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import lombok.Getter;
import org.example.model.PageCriteria;
import org.example.model.PageableSearchResult;
import org.example.model.SortCriteria;
import org.example.mybatis.annotation.TableId;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.repository.BaseRepository;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

public abstract class AbstractBaseService<T, R extends BaseRepository<T>> {

  @Getter
  private final R repository;
  private final Class<T> domainClass;

  @SuppressWarnings("unchecked")
  public AbstractBaseService(R repository) {
    this.repository = repository;
    // 获取泛型类型
    this.domainClass = (Class<T>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
  }

  public void create(T domain) {
    repository.insert(domain);
  }

  public void update(T domain) {
    repository.update(domain);
  }

  public void delete(T domain) {
    repository.delete(domain);
  }

  public List<T> select(T domain) {
    return repository.select(domain);
  }

  public List<T> advancedSelect(T domain, List<Condition> conditions, List<String> fields) {
    return repository.advancedSelect(domain, conditions, null, fields, null);
  }

  public List<T> advancedSelect(T domain, List<Condition> conditions, List<String> fields, List<SortCriteria> sorts) {
    return repository.advancedSelect(domain, conditions, sorts, fields, null);
  }

  public <U> PageableSearchResult<U> advancedSelect(T domain, List<Condition> conditions, List<String> fields,
      List<SortCriteria> sorts, PageCriteria page, Function<T, U> domainConversionFunction) {
    final long total = repository.advancedCount(domain, conditions);
    final List<T> list = repository.advancedSelect(domain, conditions, sorts, fields, page);
    PageableSearchResult<U> result = new PageableSearchResult<>();
    result.setTotalRecords(total);
    result.setTotalPages((long) Math.ceil((double) total / page.getSize()));
    result.setCurrentPageNumber(page.getCurrent());
    result.setPageSize(page.getSize());
    result.setRecords(Optional.ofNullable(list).orElseGet(ArrayList::new).stream().map(domainConversionFunction).toList());
    return result;
  }

  public long advancedCount(T domain, List<Condition> conditions) {
    return repository.advancedCount(domain, conditions);
  }

  public void batchInsert(List<T> domains) {
    repository.batchInsert(domains);
  }

  public void deleteById(BigDecimal id) {
    final T domain = createDomainWithId(id);
    repository.delete(domain);
  }

  public T selectById(BigDecimal id) {
    final T domain = createDomainWithId(id);
    final List<T> domains = repository.select(domain);
    return getFirstDomain(domains);
  }

  public T selectOne(T domain, List<Condition> conditions, List<String> fields) {
    final List<T> domains = repository.advancedSelect(domain, conditions, null, fields, null);
    return getFirstDomain(domains);
  }

  private T getFirstDomain(List<T> domains) {
    if (CollectionUtils.isEmpty(domains)) {
      throw new NoSuchElementException("No domain found");
    }
    if (domains.size() > 1) {
      throw new IllegalStateException("Multiple domains found");
    }
    return domains.get(0);
  }

  private T createDomain() {
    try {
      return domainClass.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException("Failed to create domain instance", e);
    }
  }

  private T createDomainWithId(BigDecimal id) {
    // 创建领域对象实例
    T domain = createDomain();

    // 查找并设置id字段
    Field idField = findIdField(domainClass);
    Assert.notNull(idField, "No ID field found in domain class: %s".formatted(domainClass.getName()));
    ReflectionUtils.setField(idField, domain, id);

    return domain;
  }

  /**
   * 查找ID字段 支持多种常见的ID注解和命名方式
   */
  private Field findIdField(Class<?> clazz) {
    // 1. 首先查找带有 @Id 注解的字段
    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(TableId.class)) {
        return field;
      }
    }

    // 2. 查找名为"id"的字段（不区分大小写）
    for (Field field : clazz.getDeclaredFields()) {
      if (field.getName().equalsIgnoreCase("id")) {
        return field;
      }
    }

    // 3. 如果有父类，递归查找父类的字段
    Class<?> superClass = clazz.getSuperclass();
    if (superClass != null && superClass != Object.class) {
      return findIdField(superClass);
    }

    return null;
  }

}
