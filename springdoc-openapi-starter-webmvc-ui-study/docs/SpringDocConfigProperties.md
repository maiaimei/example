# SpringDocConfigProperties

```
springdoc-openapi-starter-common-2.8.4.jar!\META-INF\spring\org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

## pathsToMatch 和 pathsToExclude

这两个属性可以通过以下几种方式配置：

使用 application.properties/application.yml 配置：

```properties
# application.properties
# 指定要包含的路径
springdoc.paths-to-match=/api/**, /public/**
# 指定要排除的路径
springdoc.paths-to-exclude=/api/admin/**, /api/internal/**
```

或者使用 YAML 格式：

```yaml
# application.yml
springdoc:
  paths-to-match:
    - /api/**
    - /public/**
  paths-to-exclude:
    - /api/admin/**
    - /api/internal/**
```

通过 Java 配置类：

```java
@Configuration
public class SpringDocConfig {
    
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/api/**", "/public/**")
                .pathsToExclude("/api/admin/**", "/api/internal/**")
                .build();
    }
}
```

如果需要更细粒度的控制，可以使用 @OpenAPIDefinition 注解：

```java
@OpenAPIDefinition(
    info = @Info(title = "API Documentation", version = "1.0"),
    servers = @Server(url = "/")
)
@Configuration
public class SpringDocConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Application API")
                        .version("1.0"));
    }
}
```

一些重要说明：

1. pathsToMatch 用于指定哪些路径应该包含在 API 文档中
2. pathsToExclude 用于指定哪些路径应该从 API 文档中排除
3. 路径匹配支持 Ant 风格的路径模式：
   - `**` 匹配零个或多个目录
   - `*` 匹配零个或多个字符
   - `?` 匹配一个字符
4. 配置的优先级：
   - pathsToExclude 的优先级高于 pathsToMatch
   - 如果同时配置了 pathsToMatch 和 pathsToExclude，会先应用 pathsToMatch，然后从结果中排除 pathsToExclude 指定的路径
5. 最佳实践：
   - 建议使用更具体的路径模式而不是过于宽泛的模式
   - 为了安全考虑，确保排除所有内部或管理API
   - 考虑按功能或访问级别对API进行分组

这些配置可以帮助你更好地控制哪些API端点应该出现在Swagger文档中，从而提供更清晰和有针对性的API文档。