package org.example.datasource;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@ConditionalOnProperty(name = "spring.datasources.enabled", havingValue = "true", matchIfMissing = false)
public class DataSourceAspect {

  @Pointcut("@annotation(org.example.datasource.DataSource) || @within(org.example.datasource.DataSource)")
  public void dataSourcePointcut() {
  }

  @Around("dataSourcePointcut()")
  public Object switchDataSource(ProceedingJoinPoint point) throws Throwable {
    // 获取方法签名
    MethodSignature signature = (MethodSignature) point.getSignature();
    // 获取方法上的注解
    DataSource dataSource = signature.getMethod().getAnnotation(DataSource.class);
    if (Objects.isNull(dataSource)) {
      // 如果方法上没有，则查找类上的注解
      dataSource = point.getTarget().getClass().getAnnotation(DataSource.class);
    }

    if (Objects.nonNull(dataSource)) {
      String type = dataSource.type();
      String value = dataSource.value();
      log.info("Switching DataSource to: {}", value);
      DataSourceContextHolder.setDataSourceType(type);
      DataSourceContextHolder.setDataSourceName(value);
      try {
        return point.proceed();
      } finally {
        DataSourceContextHolder.clearDataSourceType();
        DataSourceContextHolder.clearDataSourceName();
      }
    } else {
      return point.proceed();
    }
  }
}