package cn.maiaimei.demo.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.*;
import org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor;
import org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;

import java.util.Arrays;

/**
 * {@link AbstractAspectJAdvice}
 * {@link AspectJMethodBeforeAdvice} -> {@link MethodBeforeAdviceInterceptor}
 * {@link AspectJAfterReturningAdvice} -> {@link AfterReturningAdviceInterceptor}
 * {@link AspectJAfterThrowingAdvice}
 * {@link AspectJAfterAdvice}
 * {@link AspectJAroundAdvice}
 */
@Aspect
public class MathServiceAspect {
    private static final Logger log = LoggerFactory.getLogger(MathServiceAspect.class);

    @Pointcut("execution(public * cn.maiaimei.demo.service.MathService.*(..))")
    public void pointcut() {

    }

    @Before(value = "pointcut()")
    public void logBefore(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String clazzName = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();
        log.info("【前置通知】{}.{}, args={}", clazzName, methodName, Arrays.asList(args));
    }

    @After(value = "cn.maiaimei.demo.service.MathServiceAspect.pointcut()")
    public void logAfter(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String clazzName = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        log.info("【后置通知】{}.{}", clazzName, methodName);
    }

    @AfterReturning(value = "pointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        Signature signature = joinPoint.getSignature();
        String clazzName = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        log.info("【返回通知】{}.{}, result={}", clazzName, methodName, result);
    }

    @AfterThrowing(value = "pointcut()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        Signature signature = joinPoint.getSignature();
        String clazzName = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        log.info("【异常通知】{}.{}, exception={}", clazzName, methodName, ex);
    }

    @Around(value = "pointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String clazzName = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();
        try {
            log.info("【前置通知】{}.{}, args={}", clazzName, methodName, Arrays.asList(args));
            Object result = joinPoint.proceed();
            log.info("【返回通知】{}.{}, result={}", clazzName, methodName, result);
            return result;
        } catch (Throwable ex) {
            log.info("【异常通知】{}.{}, exception={}", clazzName, methodName, ex);
            return null;
        } finally {
            log.info("【后置通知】{}.{}", clazzName, methodName);
        }
    }
}
