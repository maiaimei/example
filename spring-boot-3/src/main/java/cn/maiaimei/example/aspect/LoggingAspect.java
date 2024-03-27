package cn.maiaimei.example.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
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

@Slf4j
@Aspect
public class LoggingAspect {

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
  public Object aroundRequestMapping(ProceedingJoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String clazzName = signature.getDeclaringTypeName();
    String methodName = signature.getName();
    Object[] args = joinPoint.getArgs();
    try {
      // 在方法执行前执行的逻辑
      log.info("【前置通知】{}.{}，参数：{}", clazzName, methodName, Arrays.asList(args));
      // 执行方法
      Object result = joinPoint.proceed();
      // 在方法执行后执行的逻辑
      log.info("【返回通知】{}.{}，结果：{}", clazzName, methodName, result);
      return result;
    } catch (Throwable ex) {
      log.error(String.format("【异常通知】%s.%s，异常：%s", clazzName, methodName, ex.getMessage()),
          ex);
      return null;
    } finally {
      log.info("【后置通知】{}.{}", clazzName, methodName);
    }
  }

}
