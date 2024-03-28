package cn.maiaimei.example.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class RequestMappingAspect {

  private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

  @Pointcut("execution(public * cn.maiaimei.example.controller.HealthCheckController.healthCheck"
      + "())")
  public void ignoreHealthCheck() {
  }

  /**
   * 控制器方法必须是标记 @RequestMapping 注解才会被增强，例如：
   *
   * @RequestMapping(value = "/demo/{num}", method = RequestMethod.GET)
   * <p>
   * 如此类推。。。
   */
  @Pointcut("execution(public * cn.maiaimei.example.controller..*(..)) && !ignoreHealthCheck()")
  public void requestMappingPointcut() {

  }

  @Before("requestMappingPointcut()")
  public void requestMappingBeforeAdvice() {
    // 记录开始时间
    START_TIME.set(System.currentTimeMillis());
    RequestAttributes ra = RequestContextHolder.getRequestAttributes();
    ServletRequestAttributes sra = (ServletRequestAttributes) ra;
    HttpServletRequest request = sra.getRequest();
    String uri = request.getRequestURI();
    String method = request.getMethod();
    // refer to https://blog.csdn.net/justlpf/article/details/108311893
    log.info("【前置通知 @Before】URI: {}, Method: {}", uri, method);
  }

  @After("requestMappingPointcut()")
  public void requestMappingAfterAdvice() {
    // 记录结束时间
    long end = System.currentTimeMillis();
    // 计算耗时
    long duration = end - START_TIME.get();
    log.info("【后置通知 @After】耗时：{}ms", duration);
    START_TIME.remove();
  }

  @AfterReturning(value = "requestMappingPointcut()", returning = "result")
  public void requestMappingAfterReturningAdvice(Object result) {
    log.info("【返回通知 @AfterReturning】结果：{}", result);
  }

  @AfterThrowing(value = "requestMappingPointcut()", throwing = "ex")
  public void requestMappingAfterThrowingAdvice(Exception ex) {
    log.error(String.format("【异常通知 @AfterThrowing】异常：%s", ex.getMessage()), ex);
  }

}
