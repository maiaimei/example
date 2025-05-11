[https://docs.spring.io/spring-boot/reference/features/logging.html](https://docs.spring.io/spring-boot/reference/features/logging.html)

[https://logback.qos.ch/](https://logback.qos.ch/)

debug=true 和在 logback 中指定包级别日志有以下主要区别：

1. 作用范围不同：

application.yml 中设置

```yaml
debug: true
```

- 这会启用 Spring Boot 的调试模式
- 只影响 Spring Boot 的核心组件日志

- 主要包括：Spring Boot、Spring MVC、Hibernate、Tomcat等框架日志

- 不会影响应用程序自身代码的日志级别

logback.xml 中设置

```xml
<configuration>
    <!-- 指定包的日志级别为DEBUG -->
    <logger name="com.example.myapp" level="DEBUG"/>
    

    <!-- 或设置根日志级别为DEBUG -->
    <root level="DEBUG">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

- 直接控制指定包或全局的日志级别

- 可以精确控制到具体包或类

- 会影响指定范围内所有的日志输出


2. 配置方式对比：

```yaml
# application.yml - Spring Boot方式
debug: true
# 或
logging:
  level:
    root: DEBUG                    # 全局日志级别
    com.example.myapp: DEBUG       # 特定包的日志级别
    org.springframework: INFO      # Spring框架的日志级别
    org.hibernate: INFO           # Hibernate的日志级别
```

```xml
<!-- logback.xml - 原生方式 -->
<configuration>
    <!-- 输出格式配置 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- 特定包的日志级别 -->
    <logger name="com.example.myapp.controller" level="DEBUG"/>
    <logger name="com.example.myapp.service" level="INFO"/>
    
    <!-- 框架日志级别 -->
    <logger name="org.springframework" level="INFO"/>
    <logger name="org.hibernate" level="INFO"/>
    
    <!-- 根日志级别 -->
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>

</configuration>
```

3. 实际应用示例：

```java
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        // 这些日志是否显示取决于包的日志级别配置
        log.debug("Fetching user with id: {}", id);
        log.info("User {} requested", id);
        
        // 业务逻辑...
        
        log.debug("User details retrieved: {}", user);
        return user;
    }

}
```

4. 日志级别继承关系：

```xml
<!-- logback.xml -->
<configuration>
    <!-- 父包设置会影响子包 -->
    <logger name="com.example" level="INFO"/>
    

    <!-- 子包可以覆盖父包设置 -->
    <logger name="com.example.myapp" level="DEBUG"/>
    
    <!-- 更具体的包配置优先级更高 -->
    <logger name="com.example.myapp.controller" level="TRACE"/>

</configuration>
```

5. 最佳实践建议：

```yaml
# application.yml - 开发环境
spring:
  profiles: dev
logging:
  level:
    root: INFO
    com.example.myapp: DEBUG
    org.springframework: INFO

---

# application.yml - 生产环境
spring:
  profiles: prod
logging:
  level:
    root: WARN
    com.example.myapp: INFO
    org.springframework: WARN
```

主要区别总结：

1. 控制范围：
   * debug=true 主要影响框架组件
   * logback配置可精确控制任意包或类

2. 灵活性：
   * debug=true 是一个开关，要么全开要么全关
   * logback配置可以针对不同包设置不同级别

3. 优先级：
   * logback的配置优先级高于application.yml
   * 更具体的包配置优先级更高

4. 使用场景：
   * debug=true 适合调试框架问题
   * logback配置适合日常开发和生产环境

5. 配置方式：
   * debug=true 配置简单，一行搞定
   * logback配置更复杂但更灵活

建议使用方式：

1. 开发调试时：
   * 使用logback配置，精确控制需要调试的包
   * 避免使用 debug=true，以免日志太多

2. 生产环境：
   * 使用logback配置，设置合适的日志级别
   * 永远不要使用 debug=true

3. 框架调试：
   * 需要调试Spring Boot框架时使用 debug=true
   * 同时可以在logback中关闭不需要的包的调试日志