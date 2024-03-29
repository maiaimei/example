package cn.maiaimei.example.aop.aspect;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
//@Component
public class HealthCheckAspect {

  @Around("execution(public * cn.maiaimei.example.controller.HealthCheckController.healthCheck())")
  public Object healthCheckAroundAdvice(ProceedingJoinPoint joinPoint) {
    // 记录开始时间
    long start = System.currentTimeMillis();
    try {
      // 在方法执行前执行的逻辑
      log.info("【前置通知 @Before】参数：{}", Arrays.asList(joinPoint.getArgs()));
      // 执行方法
      Object result = joinPoint.proceed();
      // 在方法执行后执行的逻辑
      log.info("【返回通知 @AfterReturning】结果：{}", result);
      return result;
    } catch (Throwable ex) {
      log.error(String.format("【异常通知 @AfterThrowing】异常：%s", ex.getMessage()), ex);
      return null;
    } finally {
      // 记录结束时间
      long end = System.currentTimeMillis();
      // 计算耗时
      long duration = end - start;
      log.info("【后置通知 @After】耗时：{}ms", duration);
    }
  }
}
