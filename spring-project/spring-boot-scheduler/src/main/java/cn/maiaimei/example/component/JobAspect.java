package cn.maiaimei.example.component;

import cn.maiaimei.example.constant.GlobalConstant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class JobAspect {
    private static final Logger log = LoggerFactory.getLogger(JobAspect.class);

    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MDC.put(GlobalConstant.TRACE_ID, UUID.randomUUID().toString().replaceAll("-", ""));
        String name = pjp.getSignature().getName();
        Object[] args = pjp.getArgs();
        Object result = null;
        try {
            // @Before
            log.info("【环绕前置通知】【{}方法开始】", name);
            // 相当于method.invoke(obj,args)，通过反射来执行接口中的方法
            result = pjp.proceed();
            // @AfterReturning
            log.info("【环绕返回通知】【{}方法返回】，返回值：{}", name, result);
        } catch (Exception e) {
            // @AfterThrowing
            log.error("【环绕异常通知】【{}方法异常】，参数：{}", name, args, e);
        } finally {
            // @After
            log.info("【环绕后置通知】【{}方法结束】", name);
            MDC.remove(GlobalConstant.TRACE_ID);
        }
        return result;
    }
}
