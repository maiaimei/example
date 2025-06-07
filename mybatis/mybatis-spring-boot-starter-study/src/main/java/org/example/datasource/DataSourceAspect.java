package org.example.datasource;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@ConditionalOnProperty(name = "spring.datasources.enabled", havingValue = "true", matchIfMissing = false)
public class DataSourceAspect {

  @PostConstruct
  public void test() {
    System.out.println("DataSourceAspect init...");
  }

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