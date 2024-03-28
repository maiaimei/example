package cn.maiaimei.example.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * https://docs.spring.io/spring-framework/reference/core/aop/ataspectj/pointcuts.html
 */
public class PointcutExamples {

  /**
   * 匹配所有方法，范围太广，不建议用
   */
  @Pointcut("execution(* *..*(..))")
  public void allMethod() {
  }

  /**
   * 匹配公共方法，范围太广，不建议用
   */
  @Pointcut("execution(public * *(..))")
  public void publicMethod() {
  }

  /**
   * 匹配有两个String类型参数的方法
   */
  @Pointcut("execution(* *..*(java.lang.String, java.lang.String))")
  public void stringArgs() {
  }

  /**
   * 指定方法前缀
   */
  @Pointcut("execution(* *..*.prefix*(..))")
  public void prefixMethod() {
  }

  /**
   * 指定方法后缀
   */
  @Pointcut("execution(* *..*.*Suffix(..))")
  public void suffixMethod() {
  }

  @Pointcut("execution(* *..*(..)) "
      + "&& (!execution(* *..prefix*(..)) || !execution(* *..*suffix(..)))")
  public void complexPointcut() {
  }

  /**
   * 增强被指定注解修饰的方法（所有加了@CustomAnnotation注解的方法都会被增强）
   */
  @Pointcut("@annotation(cn.maiaimei.example.annotation.CustomAnnotation)")
  public void customAnnotationMethod() {
  }

  /**
   * 匹配cn.maiaimei.example.service包及其子包的方法
   */
  @Pointcut("within(cn.maiaimei.example.service..*)")
  public void inService() {
  }

  /**
   * 匹配cn.maiaimei.example.service包及其子包的公共方法
   */
  @Pointcut("publicMethod() && inService()")
  public void publicMethodAndInService() {
  }
}
