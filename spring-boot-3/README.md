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
    * 返回通知（@AfterReturning）：在方法正常返回后执行。
    * 异常通知（@AfterThrowing）：在方法抛出异常后执行。
    * 环绕通知（@Around）：相当于try-catch-finally，可以实现以上四种通知。
* 连接点（JoinPoint）：被切入点选中的方法。这些方法会被增强处理。

### Pointcut

#### execution

匹配方法切入点。

表达式模式：

```
execution(modifier? ret-type declaring-type?name-pattern(param-pattern) tdrows-pattern?)
```

表达式解释：

- modifier：匹配修饰符，public, private 等，省略时匹配任意修饰符
- ret-type：匹配返回类型，使用 * 匹配任意类型
- declaring-type：匹配目标类，省略时匹配任意类型
  - 使用`.*`匹配包，但不匹配子包
  - 使用`..*`匹配包及子包
- name-pattern：匹配方法名称，使用 * 表示通配符
  - \* 匹配任意方法
  - set* 匹配名称以 set 开头的方法
- param-pattern：匹配参数类型和数量
  - () 匹配没有参数的方法
  - (..) 匹配有任意数量、任意类型参数的方法
  - (*) 匹配有一个任意类型参数的方法
  - (*,String) 匹配有两个参数的方法，并且第一个为任意类型，第二个为 String 类型
- tdrows-pattern：匹配抛出异常类型，多个异常以逗号分隔，省略时匹配任意类型

使用示例：

```java
// 匹配public方法
execution(public * *(..))
// 匹配名称以set开头的方法
execution(* set*(..))
// 匹配AccountService接口或类的方法
execution(* com.xyz.service.AccountService.*(..))
// 匹配service包及其子包的类或接口
execution(* com.xyz.service..*(..))
```

#### within

匹配的是类或包。不能匹配接口。

表达式模式：

```
witdin(declaring-type)
```

使用示例：

```java
// 匹配service包的类
witdin(com.xyz.service.*)
// 匹配service包及其子包的类
witdin(com.xyz.service..*)
// 匹配AccountServiceImpl类
witdin(com.xyz.service.AccountServiceImpl)
```

#### @within

匹配的是类上的注解。当类上使用了注解，类中的方法都会被增强。

#### @annotation

匹配的是方法上的注解。当方法上使用了注解，该方法就会被增强。

使用示例：

```
@annotation(com.xyz.annotation.MyAnnotation)
```

## 过滤器和拦截器

### Filter

过滤器实现方式及执行优先级如下：

1. 实现Filter接口，并添加@Component注解
2. 实现Filter接口，将过滤器添加到FilterRegistrationBean，并交给spring容器
3. 实现Filter接口，并添加@WebFilter注解，启动类加@ServletComponentScan注解，@ServletComponentScan注解可以扫描@WebFilter、@WebServlet和@WebListener注解

### HandlerInterceptor

## Logging

[https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging)

[https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging.custom-log-configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging.custom-log-configuration)

[https://logback.qos.ch/](https://logback.qos.ch/)

![](./images/20240326233255.png)

```xml
<configuration>
 
  <!-- 为特定类指定日志级别 -->
  <logger name="com.example.Class1" level="DEBUG"/>
  <logger name="com.example.Class2" level="INFO"/>
  <logger name="com.example.Class3" level="WARN"/>
    
  <logger name="com.example.Class4" level="INFO"
    additivity="false">
    <appender-ref ref="STDOUT"/>
    <appender-ref ref="FILE"/>
  </logger>
  
  <!-- 为com.example包下的所有类设置DEBUG级别 -->
  <logger name="com.example.*" level="DEBUG"/>
 
  <!-- 全局的日志级别控制，为所有其他类设置默认日志级别，若logger指定了日志级别则使用logger的日志级别，没有就跟随root的日志级别 -->
  <root level="ERROR">
    <appender-ref ref="STDOUT" />
  </root>
 
  <!-- 配置appender -->
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
  </appender>
    
  <appender name="FILE" class="ch.qos.logback.core.FileAppender">
    <file>application.log</file>
    <encoder>
      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
    <immediateFlush>true</immediateFlush>  
  </appender>
 
</configuration>
```

**Logback的配置是从上到下进行匹配的，因此当多个`<logger>`定义重叠时，具体的类或包会应用最具体（即最后定义）的配置。**

appender-ref 表示日志输出到哪些目标处。

logger中additivity的作用在于childrenLogger是否使用 rootLogger配置的appender进行输出。默认是true。

- true： 表示当前logger的appender-ref和rootLogger的appender-ref都有效。也就是说日志会打印至少两遍。
- false：表示只用当前logger的appender-ref。不会向上一层级传递日志。

通过配置`<appender>`的`immediateFlush`属性为`true`来立即刷新日志。这意味着每次日志事件被记录时，相关的appender都会立即写入其输出目标。

