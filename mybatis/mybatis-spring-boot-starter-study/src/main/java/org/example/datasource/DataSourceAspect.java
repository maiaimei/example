package org.example.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(name = "spring.datasources.enabled", havingValue = "true", matchIfMissing = false)
public class DataSourceAspect {

  @Around("@annotation(dataSource)")
  public Object around(ProceedingJoinPoint point, DataSource dataSource) throws Throwable {
    String type = dataSource.type();
    String value = dataSource.value();
    DataSourceContextHolder.setDataSourceType(type);
    DataSourceContextHolder.setDataSourceName(value);
    try {
      return point.proceed();
    } finally {
      DataSourceContextHolder.clearDataSourceType();
      DataSourceContextHolder.clearDataSourceName();
    }
  }
}