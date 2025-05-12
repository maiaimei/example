# Get Started

First, add the dependency to your `pom.xml`

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

Basic configuration in `application.properties` or `application.yml`:

```yaml
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    operationsSorter: method
```

Create a configuration class for OpenAPI documentation: 

```java
@Configuration
public class OpenAPIConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Your API Title")
                        .version("1.0")
                        .description("Your API Description")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@example.com")));
    }
}
```

Annotate your REST controllers and methods with OpenAPI annotations:

```java
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    @Operation(
        summary = "Create a new user",
        description = "Creates a new user with the provided information"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<User> createUser(
        @RequestBody @Valid User user
    ) {
        // Implementation
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
```

For your model classes, you can use annotations to enhance the documentation:

```java
public class User {
    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "User's email address", example = "user@example.com")
    @Email
    private String email;

    @Schema(description = "User's full name", example = "John Doe")
    @NotBlank
    private String name;

    // getters and setters
}
```

Common annotations you can use:

* `@Tag`: Adds a tag to group operations

* `@Operation`: Describes an API operation

* `@Parameter`: Documents a parameter

* `@Schema`: Provides additional details about model properties

* `@ApiResponse`: Documents possible responses

* `@SecurityScheme`: Defines security schemes

To enable security documentation, you can add security scheme configuration:
```java
@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenAPIConfig {
    // ... other configuration
}
```

Then use it in your controllers:

```java
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/secure")
public class SecureController {
    // ... your endpoints
}
```

After setting this up, you can access:

- The Swagger UI at: http://localhost:8080/swagger-ui.html
- The OpenAPI JSON at: http://localhost:8080/api-docs
- The OpenAPI YAML at: http://localhost:8080/api-docs.yaml

If you've set a context path (like `/api`), try accessing:

- http://localhost:8080/api/swagger-ui.html

Alternative URLs you can try:

- http://localhost:8080/swagger-ui/index.html
- http://localhost:8080/api/swagger-ui/index.html (if using /api context path)

If you want to customize the path, you can set it in your configuration:

```yaml
springdoc:
  swagger-ui:
    path: /docs  # This will make Swagger UI available at /docs
```

Additional tips:

1. You can disable the OpenAPI documentation in production by setting:

   ```yaml
   springdoc:
     api-docs:
       enabled: false
   ```

2. To group APIs, use:

   ```yaml
   springdoc:
     group-configs:
       - group: admin
         paths-to-match: /admin/**
       - group: public
         paths-to-match: /public/**
   ```

   This setup will give you a well-documented API with a nice UI for testing and exploring your endpoints.

# OpenAPI Annotations

## 文档基本信息注解

```java
@OpenAPIDefinition(
    info = @Info(
        title = "用户服务API",
        version = "1.0",
        description = "用户服务相关接口文档",
        contact = @Contact(
            name = "张三",
            email = "zhangsan@example.com",
            url = "http://example.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0"
        )
    ),
    servers = {
        @Server(
            url = "http://dev.example.com",
            description = "开发环境"
        ),
        @Server(
            url = "http://prod.example.com",
            description = "生产环境"
        )
    }
)
```

## 控制器和接口注解

```java
@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Operation(
        summary = "创建用户",
        description = "创建一个新的用户账号",
        operationId = "createUser"
    )
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        // 实现
    }

    @Operation(
        summary = "获取用户信息",
        description = "根据用户ID获取用户详细信息"
    )
    @Deprecated  // 标记废弃的API
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        // 实现
    }
}
```

## 参数注解

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
        @Parameter(
            description = "产品类别",
            required = true,
            schema = @Schema(type = "string", allowableValues = {"电子", "服装", "食品"})
        )
        @RequestParam String category,

        @Parameter(
            description = "价格范围",
            array = @ArraySchema(schema = @Schema(type = "number")),
            example = "100,1000"
        )
        @RequestParam List<BigDecimal> priceRange,

        @Parameter(
            description = "页码",
            schema = @Schema(type = "integer", minimum = "1", defaultValue = "1")
        )
        @RequestParam(defaultValue = "1") int page,

        @Parameter(
            description = "API版本",
            in = ParameterIn.HEADER,
            required = true
        )
        @RequestHeader("X-API-Version") String apiVersion
    ) {
        // 实现
    }
}
```

## 响应注解

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Operation(summary = "创建订单")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "订单创建成功",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = OrderResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "orderId": "ORD20230001",
                        "totalAmount": 299.99,
                        "status": "已确认"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "请求参数错误",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "资源未找到",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        // 实现
    }
}
```

## 模型注解

```java
@Schema(description = "用户请求对象")
public class UserRequest {

    @Schema(
        description = "用户邮箱",
        example = "zhangsan@example.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email
    private String email;

    @Schema(
        description = "用户名",
        example = "张三",
        minLength = 2,
        maxLength = 50
    )
    @NotBlank
    private String username;

    @Schema(
        description = "用户角色",
        example = "[\"ADMIN\", \"USER\"]"
    )
    private Set<String> roles;

    @Schema(
        description = "用户状态",
        example = "ACTIVE",
        allowableValues = {"ACTIVE", "INACTIVE", "BLOCKED"}
    )
    private String status;
}
```

## 安全相关注解

```java
@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                    .addSecuritySchemes("basicAuth", new SecurityScheme()
                        .type(SecuritySchemeType.HTTP)
                        .scheme("basic")
                    )
                );
    }
}

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/secure")
public class SecureController {

    @Operation(
        security = {
            @SecurityRequirement(name = "bearerAuth"),
            @SecurityRequirement(name = "basicAuth")
        }
    )
    @GetMapping("/data")
    public ResponseEntity<SecureData> getSecureData() {
        // 实现
    }
}
```

## 扩展注解

```java
@RestController
@RequestMapping("/api/extended")
public class ExtendedController {

    @Operation(
        extensions = {
            @Extension(
                name = "x-rate-limit",
                properties = {
                    @ExtensionProperty(name = "limit", value = "100"),
                    @ExtensionProperty(name = "period", value = "1 hour")
                }
            )
        }
    )
    @GetMapping("/rate-limited")
    public ResponseEntity<Data> getRateLimitedData() {
        // 实现
    }
}
```

## 分组和标签注解

```java
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/public/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/api/admin/**")
                .build();
    }
}

@Tag(name = "公共接口", description = "可公开访问的接口")
@RestController
@RequestMapping("/api/public")
public class PublicController {
    // 实现
}

@Tag(name = "管理接口", description = "仅管理员可访问的接口")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    // 实现
}
```

# OpenAPI文档管理的最佳实践和策略

## 模块化文档配置

```java
// 基础配置类
@Configuration
public class BaseOpenAPIConfig {
    
    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("企业服务平台API")
                        .version("1.0")
                        .description("企业级服务平台接口文档"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", 
                            new SecurityScheme()
                                .type(SecuritySchemeType.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}

// 用户模块配置
@Configuration
public class UserModuleOpenAPIConfig {
    
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户服务")
                .pathsToMatch("/api/users/**")
                .addOpenApiCustomizer(openApi -> openApi.info(
                    new Info()
                        .title("用户服务API")
                        .version("1.0")
                        .description("用户管理相关接口")))
                .build();
    }
}

// 订单模块配置
@Configuration
public class OrderModuleOpenAPIConfig {
    
    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("订单服务")
                .pathsToMatch("/api/orders/**")
                .build();
    }
}
```

## 通用响应和请求模型

```java
// 通用响应封装
@Schema(description = "通用响应模型")
public class ApiResponse<T> {
    
    @Schema(description = "业务状态码")
    private String code;
    
    @Schema(description = "响应消息")
    private String message;
    
    @Schema(description = "响应数据")
    private T data;
    
    // getters and setters
}

// 通用分页请求
@Schema(description = "分页查询参数")
public class PageRequest {
    
    @Schema(description = "页码", minimum = "1", defaultValue = "1")
    private Integer pageNum = 1;
    
    @Schema(description = "每页大小", minimum = "1", maximum = "100", defaultValue = "10")
    private Integer pageSize = 10;
    
    @Schema(description = "排序字段")
    private String sortField;
    
    @Schema(description = "排序方向", allowableValues = {"ASC", "DESC"})
    private String sortDirection;
}

// 通用分页响应
@Schema(description = "分页响应数据")
public class PageResponse<T> {
    
    @Schema(description = "当前页数据")
    private List<T> records;
    
    @Schema(description = "总记录数")
    private long total;
    
    @Schema(description = "当前页")
    private int current;
    
    @Schema(description = "每页大小")
    private int size;
}
```

## 统一错误码和响应处理

```java
// 错误码枚举
public enum ErrorCode {
    SUCCESS("0000", "操作成功"),
    PARAM_ERROR("4000", "请求参数错误"),
    UNAUTHORIZED("4001", "未授权"),
    FORBIDDEN("4003", "权限不足"),
    NOT_FOUND("4004", "资源不存在"),
    SYSTEM_ERROR("5000", "系统内部错误");

    private final String code;
    private final String message;
    
    // constructor and getters
}

// 统一响应处理
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    @Operation(summary = "业务异常处理")
    @ApiResponse(
        responseCode = "400",
        description = "业务处理失败",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ApiResponse.class)
        )
    )
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        // 处理逻辑
    }
}
```

## 接口版本控制

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {
    String[] value();
}

@RestController
@RequestMapping("/api/v{version}/users")
@Tag(name = "用户管理")
public class UserController {
    
    @ApiVersion({"1.0", "1.1"})
    @Operation(summary = "获取用户信息V1")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserVO>> getUserV1(@PathVariable Long id) {
        // v1版本实现
    }
    
    @ApiVersion({"2.0"})
    @Operation(summary = "获取用户信息V2")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDetailVO>> getUserV2(@PathVariable Long id) {
        // v2版本实现
    }
}
```

## 复杂业务模型文档化

```java
// 复杂业务模型
@Schema(description = "订单详情")
public class OrderDetail {
    
    @Schema(description = "订单基本信息")
    private OrderBase orderBase;
    
    @Schema(description = "订单项列表")
    private List<OrderItem> orderItems;
    
    @Schema(description = "支付信息")
    private PaymentInfo paymentInfo;
    
    @Schema(description = "物流信息")
    private LogisticsInfo logisticsInfo;
    
    // 内部类定义
    @Schema(description = "订单基本信息")
    public static class OrderBase {
        // 属性定义
    }
    
    @Schema(description = "订单项信息")
    public static class OrderItem {
        // 属性定义
    }
}

// 使用示例
@RestController
@RequestMapping("/api/orders")
@Tag(name = "订单管理")
public class OrderController {
    
    @Operation(
        summary = "创建订单",
        description = "创建新订单并返回订单详情"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "订单创建成功",
            content = @Content(schema = @Schema(implementation = OrderDetail.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "参数验证失败"
        )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDetail>> createOrder(
            @RequestBody @Valid OrderCreateRequest request) {
        // 实现逻辑
    }
}
```

## 文档扩展信息

```java
@Configuration
public class ExtendedOpenAPIConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addExtension("x-api-id", "enterprise-service")
                .addExtension("x-api-team", "platform-team")
                .addServersItem(new Server()
                    .url("https://api.example.com")
                    .description("生产环境")
                    .extensions(Collections.singletonMap(
                        "x-server-settings", 
                        Map.of("timeout", "30s", "retry", "3"))))
                .components(new Components()
                    .addSchemas("ErrorResponse", new Schema<>()
                        .description("错误响应")
                        .addProperty("code", new Schema<>().type("string"))
                        .addProperty("message", new Schema<>().type("string"))));
    }
}
```

## 安全配置分层

```java
@Configuration
public class SecurityOpenAPIConfig {
    
    @Bean
    public SecurityScheme jwtSecurityScheme() {
        return new SecurityScheme()
                .type(SecuritySchemeType.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
    
    @Bean
    public SecurityScheme basicAuthSecurityScheme() {
        return new SecurityScheme()
                .type(SecuritySchemeType.HTTP)
                .scheme("basic");
    }
    
    @Bean
    public SecurityScheme apiKeySecurityScheme() {
        return new SecurityScheme()
                .type(SecuritySchemeType.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("X-API-KEY");
    }
}
```

# 几种导出OpenAPI JSON文件的方法

## 使用 Spring Boot Actuator 端点

首先在 `application.yml` 配置：

```yaml
springdoc:
  api-docs:
    enabled: true
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html

# 如果需要将文档分组
springdoc:
  group-configs:
    - group: users
      paths-to-match: /api/users/**
    - group: orders
      paths-to-match: /api/orders/**
```

然后可以通过以下端点访问：

- 默认组：/v3/api-docs
- 特定组：/v3/api-docs?group=users

## 使用 Maven 插件

在 `pom.xml` 中添加插件：

```xml
<!-- SpringDoc OpenAPI Maven Plugin -->
<plugin>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-maven-plugin</artifactId>
    <version>1.4</version>
    <executions>
        <execution>
            <id>generate-openapi-spec</id>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <!-- API文档地址 -->
        <apiDocsUrl>http://localhost:8080/v3/api-docs</apiDocsUrl>
        <!-- 输出文件名 -->
        <outputFileName>openapi.json</outputFileName>
        <!-- 输出目录 -->
        <outputDir>${project.build.directory}</outputDir>
        <!-- 是否跳过生成 -->
        <skip>false</skip>
    </configuration>
</plugin>
```

高级配置：

```xml
<plugin>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-maven-plugin</artifactId>
    <version>1.4</version>
    <executions>
        <!-- 生成默认文档 -->
        <execution>
            <id>generate-default-spec</id>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <apiDocsUrl>http://localhost:8080/v3/api-docs</apiDocsUrl>
                <outputFileName>openapi-default.json</outputFileName>
                <outputDir>${project.build.directory}/api-docs</outputDir>
            </configuration>
        </execution>
        <!-- 生成用户服务文档 -->
        <execution>
            <id>generate-user-spec</id>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <apiDocsUrl>http://localhost:8080/v3/api-docs?group=users</apiDocsUrl>
                <outputFileName>openapi-users.json</outputFileName>
                <outputDir>${project.build.directory}/api-docs</outputDir>
            </configuration>
        </execution>
        <!-- 生成订单服务文档 -->
        <execution>
            <id>generate-order-spec</id>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <apiDocsUrl>http://localhost:8080/v3/api-docs?group=orders</apiDocsUrl>
                <outputFileName>openapi-orders.json</outputFileName>
                <outputDir>${project.build.directory}/api-docs</outputDir>
            </configuration>
        </execution>
    </executions>
    <configuration>
        <!-- 通用配置 -->
        <skip>${skipOpenAPIGeneration}</skip>
        <!-- 请求头配置 -->
        <headers>
            <Authorization>Bearer your-token</Authorization>
        </headers>
        <!-- 超时配置（毫秒） -->
        <timeout>5000</timeout>
        <!-- 重试次数 -->
        <retryCount>3</retryCount>
    </configuration>
</plugin>
```

使用 Maven 命令生成文档

```shell
# 生成文档
mvn clean springdoc-openapi:generate

# 跳过测试生成文档
mvn clean springdoc-openapi:generate -DskipTests

# 指定配置文件生成文档
mvn clean springdoc-openapi:generate -Dspring.profiles.active=dev

# Maven Wrapper 可以确保项目使用特定版本的 Maven：
# 安装 Maven Wrapper
mvn wrapper:wrapper -Dmaven=3.9.6
```

集成到构建生命周期

```xml
<plugin>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-maven-plugin</artifactId>
    <version>1.4</version>
    <executions>
        <execution>
            <phase>process-classes</phase>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

自定义文档生成

```xml
<plugin>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-maven-plugin</artifactId>
    <version>1.4</version>
    <configuration>
        <!-- 自定义配置 -->
        <apiDocsUrl>http://localhost:${server.port}/v3/api-docs</apiDocsUrl>
        <outputFileName>openapi-${project.version}.json</outputFileName>
        <outputDir>${project.build.directory}/api-docs</outputDir>
        <!-- 环境变量 -->
        <environmentVariables>
            <SPRING_PROFILES_ACTIVE>dev</SPRING_PROFILES_ACTIVE>
        </environmentVariables>
        <!-- JVM参数 -->
        <jvmArguments>
            -Xmx512m
            -Dspring.profiles.active=dev
        </jvmArguments>
    </configuration>
</plugin>
```

## 使用程序化方式导出

```java
@RestController
@RequestMapping("/api")
public class OpenApiExportController {

    private final OpenAPI openAPI;

    public OpenApiExportController(OpenAPI openAPI) {
        this.openAPI = openAPI;
    }

    @GetMapping("/export-openapi")
    public ResponseEntity<String> exportOpenApi() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String openApiJson = objectMapper.writeValueAsString(openAPI);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDispositionFormData("attachment", "openapi.json");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(openApiJson);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
```

## 使用配置类自动导出

```java
@Configuration
public class OpenApiExportConfig {

    @Value("${openapi.export.path:src/main/resources/openapi}")
    private String exportPath;

    @PostConstruct
    public void exportOpenApi() throws IOException {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("API Documentation")
                        .version("1.0.0")
                        .description("API Documentation"));

        // 添加路径和组件
        Paths paths = new Paths();
        Components components = new Components();
        
        // 配置路径和组件
        openAPI.setPaths(paths);
        openAPI.setComponents(components);

        // 转换为JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        // 确保目录存在
        File directory = new File(exportPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 写入文件
        File outputFile = new File(directory, "openapi.json");
        objectMapper.writerWithDefaultPrettyPrinter()
                   .writeValue(outputFile, openAPI);
    }
}
```

## 使用命令行工具

```java
@Component
public class OpenApiExporter implements CommandLineRunner {

    private final OpenAPI openAPI;
    
    public OpenApiExporter(OpenAPI openAPI) {
        this.openAPI = openAPI;
    }

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        String json = objectMapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(openAPI);
        
        Files.write(Paths.get("openapi.json"), 
                   json.getBytes(StandardCharsets.UTF_8));
    }
}
```

## 支持多种格式导出

```java
@RestController
@RequestMapping("/api/docs")
public class ApiDocController {

    private final OpenAPI openAPI;

    public ApiDocController(OpenAPI openAPI) {
        this.openAPI = openAPI;
    }

    @GetMapping(value = "/export", produces = "application/json")
    public ResponseEntity<String> exportJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(openAPI);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDispositionFormData("attachment", "openapi.json");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(json);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/export", produces = "application/yaml")
    public ResponseEntity<String> exportYaml() {
        try {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            String yaml = objectMapper.writeValueAsString(openAPI);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, "application/yaml");
            headers.setContentDispositionFormData("attachment", "openapi.yaml");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(yaml);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
```

使用建议：

1. 选择合适的导出方式：
   - 开发环境：使用 Actuator 端点或 Swagger UI
   - CI/CD：使用 Maven 插件
   - 自定义需求：使用程序化方式
2. 注意事项：
   - 确保导出的文档完整性
   - 验证JSON格式正确性
   - 考虑文档版本管理
   - 注意敏感信息处理
3. 最佳实践：
   - 定期更新文档
   - 保持文档与代码同步
   - 进行文档版本控制
   - 添加适当的安全控制
4. 安全考虑：
   - 生产环境建议禁用在线文档
   - 控制文档访问权限
   - 避免暴露敏感信息

# Reference

OpenAPI Specification (OAS) 的官方网站有几个重要的资源地址：

1. 主要官方资源： [[1\]](https://aws.amazon.com/blogs/devops/deploy-and-manage-openapi-swagger-restful-apis-with-the-aws-cloud-development-kit/)

- OpenAPI Initiative (OAI) 官网：openapis.org [[2\]](https://docs.aws.amazon.com/prescriptive-guidance/latest/micro-frontends-aws/api-contracts.html)
- OpenAPI Specification GitHub 仓库：github.com/OAI/OpenAPI-Specification

2. 重要的相关工具和资源：

- Swagger 工具套件：swagger.io
- Swagger Editor：editor.swagger.io
- Swagger UI：swagger.io/tools/swagger-ui

3. 规范文档：

- OpenAPI 3.0 规范文档可以在 OAI GitHub 仓库中找到
- Swagger 2.0 (旧版 OpenAPI) 文档也可在同一仓库获取 [[3\]](https://docs.aws.amazon.com/serverless/latest/devguide/starter-apigw.html)

主要内容包括：

- 规范定义和更新
- 实现指南
- 工具和库
- 社区贡献
- 最佳实践
- 示例文档

建议：

1. 关注官方 GitHub 仓库获取最新更新
2. 使用官方工具进行开发和测试
3. 参考官方示例进行学习
4. 遵循规范建议的最佳实践

# Issues

## The plugin org.apache.maven.plugins:maven-clean-plugin:3.4.1 requires Maven version 3.6.3

这个错误表明当前使用的 Maven 版本低于插件所需的版本。以下是几种解决方案：

1. **升级 Maven 版本（推荐）**

   首先检查当前 Maven 版本：

   ```shell
   mvn -version
   ```

   然后下载并安装新版本的 Maven：

   ```shell
   # Windows: 
   # 1. 下载最新版 Maven
   # 2. 解压到指定目录
   # 3. 更新环境变量 MAVEN_HOME 和 Path
   
   # Linux:
   wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
   tar -xvf apache-maven-3.9.6-bin.tar.gz
   mv apache-maven-3.9.6 /opt/
   export PATH=/opt/apache-maven-3.9.6/bin:$PATH
   ```

2. **使用 Maven Wrapper（推荐）**

   ```shell
   # 在项目根目录执行
   mvn wrapper:wrapper -Dmaven=3.9.6
   ```

   然后在 IDEA 中：

   ```
   Settings -> Build Tools -> Maven
   选择 "Use Maven Wrapper"
   ```

   



## 