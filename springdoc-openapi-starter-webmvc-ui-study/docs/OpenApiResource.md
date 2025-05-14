SpringDoc OpenAPI 在 `org.springdoc.webmvc.api.OpenApiResource` 类中生成 OpenAPI 文档。主要方法是 `openapiJson()` 和 `openapiYaml()`。让我们看一下关键代码：

```java
// org.springdoc.webmvc.api.OpenApiResource 类的关键部分

@RestController
@Hidden
public class OpenApiResource {

    // OpenAPI JSON 端点
    @Operation(hidden = true)
    @GetMapping(value = API_DOCS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public byte[] openapiJson(HttpServletRequest request, @Value(API_DOCS_URL) String apiDocsUrl)
            throws JsonProcessingException {
        calculateServerUrl(request, apiDocsUrl);
        OpenAPI openAPI = this.getOpenApi();
        return JsonMapper.writeValueAsBytes(openAPI);
    }

    // OpenAPI YAML 端点
    @Operation(hidden = true)
    @GetMapping(value = DEFAULT_API_DOCS_URL_YAML, produces = APPLICATION_OPENAPI_YAML)
    public byte[] openapiYaml(HttpServletRequest request, @Value(DEFAULT_API_DOCS_URL_YAML) String apiDocsUrl)
            throws JsonProcessingException {
        calculateServerUrl(request, apiDocsUrl);
        OpenAPI openAPI = this.getOpenApi();
        return YAMLMapper.writeValueAsBytes(openAPI);
    }
}
```

我们可以通过扩展这个功能，创建一个自定义的控制器来生成和保存文档：

```java
@RestController
@RequestMapping("/api-docs")
@Hidden
@Slf4j
public class CustomOpenApiResource {

    private final OpenApiResource openApiResource;
    private final ObjectMapper objectMapper;
    private final YAMLMapper yamlMapper;

    public CustomOpenApiResource(OpenApiResource openApiResource) {
        this.openApiResource = openApiResource;
        this.objectMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.yamlMapper = new YAMLMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @GetMapping("/generate")
    public ResponseEntity<String> generateApiDocs(HttpServletRequest request) {
        try {
            // 生成并保存 JSON 格式
            generateJsonDocs(request);
            
            // 生成并保存 YAML 格式
            generateYamlDocs(request);
            
            return ResponseEntity.ok("API documentation generated successfully");
        } catch (Exception e) {
            log.error("Failed to generate API documentation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to generate API documentation: " + e.getMessage());
        }
    }

    private void generateJsonDocs(HttpServletRequest request) throws Exception {
        // 获取OpenAPI文档
        byte[] jsonBytes = openApiResource.openapiJson(request, "/v3/api-docs");
        
        // 格式化JSON
        Object jsonObject = objectMapper.readValue(jsonBytes, Object.class);
        String prettyJson = objectMapper.writeValueAsString(jsonObject);
        
        // 保存到文件
        Path jsonPath = Paths.get("api-docs.json");
        Files.write(jsonPath, prettyJson.getBytes(StandardCharsets.UTF_8));
        log.info("Generated JSON API docs at: {}", jsonPath.toAbsolutePath());
        
        // 打印到日志
        log.info("Generated OpenAPI JSON:\n{}", prettyJson);
    }

    private void generateYamlDocs(HttpServletRequest request) throws Exception {
        // 获取OpenAPI文档
        byte[] yamlBytes = openApiResource.openapiYaml(request, "/v3/api-docs.yaml");
        
        // 转换为对象并重新格式化
        Object yamlObject = yamlMapper.readValue(yamlBytes, Object.class);
        String yamlContent = yamlMapper.writeValueAsString(yamlObject);
        
        // 保存到文件
        Path yamlPath = Paths.get("api-docs.yaml");
        Files.write(yamlPath, yamlContent.getBytes(StandardCharsets.UTF_8));
        log.info("Generated YAML API docs at: {}", yamlPath.toAbsolutePath());
        
        // 打印到日志
        log.info("Generated OpenAPI YAML:\n{}", yamlContent);
    }
}
```

配置类：

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public CustomOpenApiResource customOpenApiResource(OpenApiResource openApiResource) {
        return new CustomOpenApiResource(openApiResource);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Documentation")
                        .version("1.0.0")
                        .description("Automatically generated API Documentation"));
    }
}
```

application.yml 配置：

```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
    enabled: true
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
  packages-to-scan: com.example.controller
  paths-to-match: /**
```

使用方法：

1. 启动应用后，访问以下端点：
   - JSON格式： `http://localhost:8080/v3/api-docs`
   - YAML格式： `http://localhost:8080/v3/api-docs.yaml`
   - Swagger UI： `http://localhost:8080/swagger-ui.html`
   - 生成文件： `http://localhost:8080/api-docs/generate`
2. 生成的文件将保存在项目根目录：
   - api-docs.json
   - api-docs.yaml
3. 文档内容也会打印到日志中

你也可以通过编程方式获取 OpenAPI 文档：

```java
@Service
@Slf4j
public class OpenApiService {

    private final OpenApiResource openApiResource;
    private final ObjectMapper objectMapper;
    private final YAMLMapper yamlMapper;

    public OpenApiService(OpenApiResource openApiResource) {
        this.openApiResource = openApiResource;
        this.objectMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(SerializationFeature.INDENT_OUTPUT);
        this.yamlMapper = new YAMLMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public OpenAPI getOpenApi() {
        try {
            // 创建模拟请求
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setScheme("http");
            request.setServerName("localhost");
            request.setServerPort(8080);
            
            // 获取JSON格式的文档
            byte[] jsonBytes = openApiResource.openapiJson(request, "/v3/api-docs");
            
            // 转换为OpenAPI对象
            return objectMapper.readValue(jsonBytes, OpenAPI.class);
        } catch (Exception e) {
            log.error("Failed to get OpenAPI documentation", e);
            throw new RuntimeException("Failed to get OpenAPI documentation", e);
        }
    }

    public void generateApiDocs() {
        try {
            OpenAPI openAPI = getOpenApi();
            
            // 保存JSON格式
            String jsonContent = objectMapper.writeValueAsString(openAPI);
            Files.write(Paths.get("api-docs.json"), jsonContent.getBytes(StandardCharsets.UTF_8));
            
            // 保存YAML格式
            String yamlContent = yamlMapper.writeValueAsString(openAPI);
            Files.write(Paths.get("api-docs.yaml"), yamlContent.getBytes(StandardCharsets.UTF_8));
            
            log.info("API documentation generated successfully");
        } catch (Exception e) {
            log.error("Failed to generate API documentation", e);
            throw new RuntimeException("Failed to generate API documentation", e);
        }
    }
}
```

这样你就可以：

1. 通过 Web 端点获取文档
2. 将文档保存为文件
3. 在日志中查看文档内容
4. 以编程方式处理 OpenAPI 文档

文档生成的时机：

1. 应用启动时（通过配置）
2. 手动触发（通过端点）
3. 编程方式调用

生成的文档包含：

- API 路径和方法
- 请求参数和响应
- 数据模型定义
- 安全配置
- 服务器信息

你可以根据需要自定义文档内容和格式。