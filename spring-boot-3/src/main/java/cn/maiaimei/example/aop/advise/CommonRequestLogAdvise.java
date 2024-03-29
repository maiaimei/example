package cn.maiaimei.example.aop.advise;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class CommonRequestLogAdvise extends AbstractRequestLogAdvise {

  private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

  @Before("requestMappingMethods() && !healthCheck()")
  public void logBeforeRequest() {
    // 记录开始时间
    START_TIME.set(System.currentTimeMillis());
    RequestAttributes ra = RequestContextHolder.getRequestAttributes();
    ServletRequestAttributes sra = (ServletRequestAttributes) ra;
    HttpServletRequest request = sra.getRequest();
    String uri = request.getRequestURI();
    String method = request.getMethod();
    // refer to https://blog.csdn.net/justlpf/article/details/108311893
    log.info("【前置通知1】URI: {}, Method: {}", uri, method);
  }

  @After("requestMappingMethods() && !healthCheck()")
  public void logAfterRequest() {
    // 记录结束时间
    long end = System.currentTimeMillis();
    // 计算耗时
    long duration = end - START_TIME.get();
    log.info("【后置通知1】耗时：{}ms", duration);
    START_TIME.remove();
  }

  @AfterReturning(value = "requestMappingMethods() && !healthCheck()", returning = "result")
  public void logAfterReturning(Object result) {
    log.info("【返回通知1】结果：{}", result);
  }

  @AfterThrowing(value = "requestMappingMethods() && !healthCheck()", throwing = "ex")
  public void logAfterThrowing(Exception ex) {
    log.error(String.format("【异常通知1】异常：%s", ex.getMessage()), ex);
  }

  @Around(value = "requestMappingMethods() && !healthCheck()")
  public Object logRequest(ProceedingJoinPoint joinPoint) {
    // 记录开始时间
    long start = System.currentTimeMillis();
    try {
      // 在方法执行前执行的逻辑
      log.info("【前置通知2】参数：{}", Arrays.asList(joinPoint.getArgs()));
      // 执行方法
      Object result = joinPoint.proceed();
      // 在方法执行后执行的逻辑
      log.info("【返回通知2】结果：{}", result);
      return result;
    } catch (Throwable ex) {
      log.error(String.format("【异常通知2】异常：%s", ex.getMessage()), ex);
      return null;
    } finally {
      // 记录结束时间
      long end = System.currentTimeMillis();
      // 计算耗时
      long duration = end - start;
      log.info("【后置通知2】耗时：{}ms", duration);
    }
  }

}
