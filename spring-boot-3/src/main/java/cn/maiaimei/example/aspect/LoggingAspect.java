package cn.maiaimei.example.aspect;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  public LoggingAspect() {
    log.info("LoggingAspect 初始化");
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
  public void pointcutRequestMapping() {

  }

  @Before("pointcutRequestMapping()")
  public void beforeRequestMapping(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    log.info("Before executing the method {}", method.getName());
  }

  @After("pointcutRequestMapping()")
  public void afterRequestMapping(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    log.info("After executing the method {}", method.getName());
  }

  @AfterReturning(value = "pointcutRequestMapping()", returning = "result")
  public void afterReturnRequestMapping(JoinPoint joinPoint, Object result) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    log.info("After the method {} completes normally, result is {}", method.getName(), result);
  }

  @AfterThrowing(value = "pointcutRequestMapping()", throwing = "e")
  public void afterThrowRequestMapping(JoinPoint joinPoint, Exception e) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    log.info("After the method {} exits by throwing an exception, exception is {}",
        method.getName(),
        e.getMessage());
  }

  @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
  public Object aroundRequestMapping(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    // 在方法执行前执行的逻辑
    log.info("===> Before executing the method {}", method.getName());
    // 执行方法
    Object result = joinPoint.proceed();
    // 在方法执行后执行的逻辑
    log.info("<=== After executing the method {}", method.getName());
    return result;
  }

  @Around("execution(* cn.maiaimei.example.*.*(..))")
  public Object doAroundAdviceForControllerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    // 在方法执行前执行的逻辑
    log.info("===> Before executing the method {}", method.getName());
    // 执行方法
    Object result = joinPoint.proceed();
    // 在方法执行后执行的逻辑
    log.info("<=== After executing the method {}", method.getName());
    return result;
  }

}
