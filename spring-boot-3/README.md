## AOP

[https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.aop](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.aop)

[https://docs.spring.io/spring-framework/reference/core/aop.html](https://docs.spring.io/spring-framework/reference/core/aop.html)

[https://docs.spring.io/spring-framework/reference/core/aop/introduction-defn.html](https://docs.spring.io/spring-framework/reference/core/aop/introduction-defn.html)

[https://docs.spring.io/spring-framework/reference/core/aop/ataspectj.html](https://docs.spring.io/spring-framework/reference/core/aop/ataspectj.html)

AOP（Aspect Oriented Programming）意为：面向切面编程，通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术。

* 切面（Aspect）：一般是指被@Aspect修饰的类，代表着某一具体功能的AOP逻辑。
* 切入点（Pointcut）：选择对哪些方法进行增强。
* 通知（Advice）：对目标方法的增强，有以下五种增强的类型：
    * 前置通知（@Before）：在方法执行前执行。
    * 后置通知（@After）：在方法执行后执行。
    * 返回通知（@AfterReturning）：在方法返回后执行。
    * 异常通知（@AfterThrowing）：在方法抛出异常后执行。
    * 环绕通知（@Around）：内部执行方法，可自定义在方法执行的前后操作。
* 连接点（JoinPoint）：被切入点选中的方法。这些方法会被增强处理。

[https://blog.csdn.net/qq_43331014/article/details/132867178](https://blog.csdn.net/qq_43331014/article/details/132867178)

## 拦截器和过滤器

HandlerInterceptor

Filter

## Logging

[https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging)

[https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging.custom-log-configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging.custom-log-configuration)

[https://logback.qos.ch/](https://logback.qos.ch/)

![](./images/20240326233255.png)
