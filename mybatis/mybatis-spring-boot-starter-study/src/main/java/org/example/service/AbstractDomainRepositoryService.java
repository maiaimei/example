package org.example.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import org.example.mybatis.annotation.Id;
import org.example.mybatis.query.filter.Filterable;
import org.example.repository.BasicRepository;

@Getter
public abstract class AbstractDomainRepositoryService<T, R extends BasicRepository<T>> {

  private final R repository;
  private final Class<T> domainClass;

  @SuppressWarnings("unchecked")
  public AbstractDomainRepositoryService(R repository) {
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

  public void deleteById(BigDecimal id) {
    final T domain = createDomainWithId(id);
    repository.delete(domain);
  }

  public T select(T domain) {
    return repository.select(domain);
  }

  public T selectById(BigDecimal id) {
    final T domain = createDomainWithId(id);
    return repository.select(domain);
  }

  public List<T> selectByConditions(Filterable filterable) {
    final T domain = createDomain();
    return repository.selectByConditions(domain, filterable);
  }

  public List<T> selectByConditions(T domain, Filterable filterable) {
    return repository.selectByConditions(domain, filterable);
  }

  public void batchInsert(List<T> domains) {
    // TODO: 分批插入
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
    if (idField != null) {
      idField.setAccessible(true);
      try {
        idField.set(domain, id);
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Failed to set ID field in domain class: %s".formatted(domainClass.getName()), e);
      }
    } else {
      throw new IllegalStateException("No ID field found in domain class: %s".formatted(domainClass.getName()));
    }

    return domain;
  }

  /**
   * 查找ID字段 支持多种常见的ID注解和命名方式
   */
  private Field findIdField(Class<?> clazz) {
    // 1. 首先查找带有 @Id 注解的字段
    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(Id.class)) {
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
