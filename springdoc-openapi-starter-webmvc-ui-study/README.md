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

- The Swagger UI at: http://localhost:8080/api/swagger-ui.html
- The OpenAPI JSON at: http://localhost:8080/api/api-docs
- The OpenAPI YAML at: http://localhost:8080/api/api-docs.yaml

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

# 生成不同版本的 OpenAPI 规范文档（如 OpenAPI 2.0/3.0/3.1）

指定某个版本的 OpenAPI 规范文档，在 application.yml 中配置：

```yaml
springdoc:
  api-docs:
    version: openapi_3_1  # 可选值: openapi_3_0, openapi_3_1
```

动态切换不同版本的 OpenAPI 规范文档，在Java中配置：

```java
  @Bean
  public GroupedOpenApi openAPI30Docs() {
    return GroupedOpenApi.builder()
        .group("v3.0")
        .displayName("OpenAPI 3.0")
        .pathsToMatch("/**")
        .packagesToScan("org.example")
        .addOpenApiCustomizer(openApi -> {
          // 设置为 OpenAPI 3.0
          openApi.specVersion(SpecVersion.V30);
          // 强制设置 openapi 版本为 3.0.1
          openApi.openapi("3.0.1");
        })
        .build();
  }

  @Bean
  public GroupedOpenApi openAPI31Docs() {
    return GroupedOpenApi.builder()
        .group("v3.1")
        .displayName("OpenAPI 3.1")
        .pathsToMatch("/**")
        .packagesToScan("org.example")
        .addOpenApiCustomizer(openApi -> {
          // 设置为 OpenAPI 3.1
          openApi.specVersion(SpecVersion.V31);
          // 强制设置 openapi 版本为 3.1.0
          openApi.openapi("3.1.0");
        })
        .build();
  }
```

访问不同格式和版本的文档：

JSON 格式：

- OpenAPI 3.0 JSON: `/v3/api-docs/v3.0`
- OpenAPI 3.1 JSON: `/v3/api-docs/v3.1`

YAML 格式：

- OpenAPI 3.0 YAML: `/v3/api-docs.yaml/v3.0`
- OpenAPI 3.1 YAML: `/v3/api-docs.yaml/v3.1`

Swagger UI：

- `/swagger-ui.html` \- 可以在界面上切换不同版本

# Reference

[https://github.com/springdoc/springdoc-openapi](https://github.com/springdoc/springdoc-openapi)