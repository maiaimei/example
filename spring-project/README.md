# 数据库

## H2数据库

pom.xml依赖：

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>1.4.200</version>
</dependency>
```

连接字符串：

1、内存模式

```
jdbc:h2:mem:<databaseName>

jdbc:h2:mem:test
jdbc:h2:mem:test;database_to_upper=false // 设置表名和字段名区分大小写
```

2、以嵌入式(本地)连接方式连接H2数据库

```
jdbc:h2:[file:][<path>]<databaseName>

jdbc:h2:~/test    		// 连接位于用户目录下的test数据库
jdbc:h2:file:/data/test
jdbc:h2:file:E:/data/test  // Windows only
```

3、使用TCP/IP的服务器模式(远程连接)方式连接H2数据库(推荐)

```
jdbc:h2:tcp://<server>[:<port>]/[<path>]<databaseName>

jdbc:h2:tcp://localhost/~/test
```

## MySQL数据库

pom.xml依赖：

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.28</version>
</dependency>
```
连接字符串：
```
jdbc:mysql://localhost/test
jdbc:mysql://192.168.1.12/uuap?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8
```

## Oracle数据库

pom.xml依赖：

```xml
<dependency>
    <groupId>com.oracle.jdbc</groupId>
    <artifactId>ojdbc8</artifactId>
    <version>12.2.0.1</version>
</dependency>
```
连接字符串：
```
jdbc:oracle:thin:@//server:port/serverName
```

# Q&A

## logback打印两次

由于`<logger name="xxx" level="debug" additivity="true">`的属性additivity设置为true，使打印信息传递到root去，所以打印两次。改additivity为false即可。

## 忽略NULL属性值，不传递给前端

局部解决，使用 ```@JsonInclude``(JsonInclude.Include.NON_NULL)``` 标记在字段上

全局解决，在 yml 文件中添加配置

```yaml
spring:
  jackson:
    default-property-inclusion: non_null
```

## 前端Long字段精度丢失问题

局部解决，使用 ```@JsonSerialize(using = ToStringSerializer.class)``` 标记在字段上

全局解决，添加如下配置

```java
@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }
}
```
