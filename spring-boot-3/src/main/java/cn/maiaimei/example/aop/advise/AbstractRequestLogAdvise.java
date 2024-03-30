package cn.maiaimei.example.aop.advise;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
public class AbstractRequestLogAdvise {

  @Pointcut("execution(public * *..*.ServiceCenterController.*())")
  public void serviceCenterMethods() {
  }

  @Pointcut("execution(public * *..*.ServiceCenterController.heartbeat())")
  public void heartbeat() {
  }

  @Pointcut("execution(public * *..*.HealthCheckController.healthCheck"
      + "())")
  public void healthCheck() {
  }

  @Pointcut("within(cn.maiaimei.example..*)")
  public void myPackage() {
  }

  //@Pointcut("within(@org.springframework.web.bind.annotation.RequestMapping *)")
  @Pointcut("@within(org.springframework.web.bind.annotation.RequestMapping)")
  public void classLevelRequestMapping() {
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
  public void methodLevelRequestMapping() {
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
  public void getMapping() {
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
  public void postMapping() {
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
  public void putMapping() {
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
  public void patchMapping() {
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
  public void deleteMapping() {
  }

  @Pointcut("myPackage() && (classLevelRequestMapping() || methodLevelRequestMapping() "
      + "|| getMapping() || postMapping() || putMapping() || patchMapping() || deleteMapping())")
  public void requestMappingMethods() {
  }

}
