package org.example.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
//@ConditionalOnProperty(name = "spring.datasources.enabled", havingValue = "true", matchIfMissing = false)
public class DataSourceAspect {

  @Around("@annotation(dataSource)")
  public Object switchDataSource(ProceedingJoinPoint point, DataSource dataSource) throws Throwable {
    log.info("switchDataSource is executing...");
    String type = dataSource.type();
    String value = dataSource.value();
    log.info("data source type is {}, value is {}", type, value);
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