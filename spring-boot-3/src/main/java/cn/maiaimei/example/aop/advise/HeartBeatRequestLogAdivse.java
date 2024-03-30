package cn.maiaimei.example.aop.advise;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
//@Component
public class HeartBeatRequestLogAdivse extends AbstractRequestLogAdvise {

  private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

  @Pointcut("requestMappingMethods() && heartbeat()")
  public void heartbeatRequestMethod() {
  }

  @Before("heartbeatRequestMethod()")
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

  @After("heartbeatRequestMethod()")
  public void logAfterRequest() {
    // 记录结束时间
    long end = System.currentTimeMillis();
    // 计算耗时
    long duration = end - START_TIME.get();
    log.info("【后置通知1】耗时：{}ms", duration);
    START_TIME.remove();
  }
}
