package org.example.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAspect {

  @Around("@annotation(dataSource)")
  public Object around(ProceedingJoinPoint point, DataSource dataSource) throws Throwable {
    String value = dataSource.value();
    DataSourceContextHolder.set(value);
    try {
      return point.proceed();
    } finally {
      DataSourceContextHolder.clear();
    }
  }
}