package org.example.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.example.annotation.SkipResponseWrapper;
import org.example.openapi.OpenAPIAdvancedCustomizer;
import org.example.utils.DateTimeUtils;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.HandlerMethod;

/**
 * {@link OpenApiResource#openapiJson(HttpServletRequest, String, Locale)}
 * {@link OpenApiResource#openapiYaml(HttpServletRequest, String, Locale)}
 * {@link ObjectMapperProvider#ObjectMapperProvider(SpringDocConfigProperties)}
 */
@Slf4j
@Configuration
public class OpenAPIConfig {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private OpenAPIAdvancedCustomizer openAPIAdvancedCustomizer;

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("API Documentation")
            .version("1.0.0")
            .description("REST API Documentation")
            .termsOfService("http://terms.service.url")
            .license(new License()
                .name("Apache 2.0")
                .url("http://license.url"))
            .contact(new Contact()
                .name("API Support")
                .email("support@example.com")))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(new Components()
            .addSecuritySchemes("bearerAuth", new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"))
            .addSecuritySchemes("basicAuth", new SecurityScheme()
                .name("basicAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic")))
        .externalDocs(new ExternalDocumentation()
            .description("Additional Documentation")
            .url("https://example.com/docs"));
  }

  @Bean
  public GroupedOpenApi openApi30Docs() {
    return GroupedOpenApi.builder()
        .group("v3.0")
        .displayName("OpenAPI 3.0")
        .pathsToMatch("/**")
        .packagesToScan("org.example")
        .addOperationCustomizer(operationCustomizer())
        // 添加这一行，关联 OpenApiCustomizer
        .addOpenApiCustomizer(openApiCustomizer())
        .addOpenApiCustomizer(openApi -> {
          // 设置为 OpenAPI 3.0
          openApi.specVersion(SpecVersion.V30);
          // 强制设置 openapi 版本为 3.0.1
          openApi.openapi("3.0.1");
        })
        .build();
  }

  @Bean
  public GroupedOpenApi openApi31Docs() {
    return GroupedOpenApi.builder()
        .group("v3.1")
        .displayName("OpenAPI 3.1")
        .pathsToMatch("/**")
        .packagesToScan("org.example")
        .addOperationCustomizer(operationCustomizer())
        // 添加这一行，关联 OpenApiCustomizer
        .addOpenApiCustomizer(openApiCustomizer())
        .addOpenApiCustomizer(openApi -> {
          // 设置为 OpenAPI 3.1
          openApi.specVersion(SpecVersion.V31);
          // 强制设置 openapi 版本为 3.1.0
          openApi.openapi("3.1.0");
        })
        .build();
  }

  @Bean
  public OperationCustomizer operationCustomizer() {
    return (operation, handlerMethod) -> {
      // 获取类名
      String className = handlerMethod.getBeanType().getName();
      // 获取方法名
      String methodName = handlerMethod.getMethod().getName();
      // 设置 operationId 为 类名_方法名
      operation.setOperationId(className + "_" + methodName);
      return operation;
    };
  }

  @Bean
  public OpenApiCustomizer openApiCustomizer() {
    return openAPI -> {
      // 创建统一响应的schema
      final Schema<?> errorFieldSchema = createErrorFieldSchema();
      final Schema<?> errorInfoSchema = createErrorInfoSchema();
      final Schema<?> successResponseSchema = createSuccessResponseSchema();
      final Schema<?> errorResponseSchema = createErrorResponseSchema(errorFieldSchema, errorInfoSchema);

      // 创建统一响应示例
      final Example successExample = createOkExample();
      final Example badRequestErrorExample = createBadRequestExample();

      // 遍历所有路径
      openAPI.getPaths().forEach((path, pathItem) -> {
        // 处理所有HTTP方法
        Stream.of(pathItem.getGet(), pathItem.getPost(), pathItem.getPut(),
                pathItem.getDelete(), pathItem.getPatch())
            .filter(Objects::nonNull)
            .forEach(operation -> {
              // 使用 operationId 获取对应的 HandlerMethod
              String operationId = operation.getOperationId();
              if (operationId != null) {
                try {
                  // 解析 operationId 获取类名和方法名
                  String[] parts = operationId.split("_");
                  if (parts.length == 2) {
                    String className = parts[0];
                    String methodName = parts[1];

                    // 获取对应的 Controller 类
                    Class<?> controllerClass = Class.forName(className);

                    // 查找匹配的方法
                    Method[] methods = controllerClass.getDeclaredMethods();
                    Optional<Method> targetMethod = Arrays.stream(methods)
                        .filter(m -> m.getName().equals(methodName))
                        .findFirst();

                    if (targetMethod.isPresent()) {
                      Method method = targetMethod.get();
                      HandlerMethod handlerMethod = new HandlerMethod(
                          applicationContext.getBean(controllerClass),
                          method
                      );

                      // 检查是否需要包装响应
                      if (shouldWrapResponse(handlerMethod)) {
                        // 获取原始响应
                        ApiResponse originalResponse = operation.getResponses().get("200");
                        if (originalResponse != null && originalResponse.getContent() != null) {
                          MediaType mediaType = originalResponse.getContent().get("application/json");
                          if (mediaType != null && mediaType.getSchema() != null) {
                            Schema<?> originalSchema = mediaType.getSchema();

                            // 创建成功响应的包装 Schema
                            Schema<?> wrappedSuccessSchema = new Schema<>()
                                .type("object")
                                .$ref("#/components/schemas/SuccessApiResponse");

                            // 设置 data 属性为原始 Schema
                            ((Schema<?>) openAPI.getComponents()
                                .getSchemas()
                                .get("SuccessApiResponse"))
                                .getProperties()
                                .put("data", originalSchema);

                            // 更新响应
                            Content content = new Content()
                                .addMediaType("application/json",
                                    new MediaType()
                                        .schema(wrappedSuccessSchema)
                                        .addExamples("200", successExample));

                            operation.getResponses().put("200", new ApiResponse().content(content));
                          }
                        }
                      }
                    }
                  }
                } catch (Exception e) {
                  // 处理异常
                  log.warn("Failed to process operation: " + operationId, e);
                }
              }
            });
      });

      // 添加统一响应schema到组件
      openAPI.getComponents().addSchemas("SuccessApiResponse", successResponseSchema);
      openAPI.getComponents().addSchemas("ErrorApiResponse", errorResponseSchema);
      openAPI.getComponents().addSchemas("ErrorInfo", errorInfoSchema);
      openAPI.getComponents().addSchemas("ErrorField", errorFieldSchema);
    };
  }

  private boolean shouldWrapResponse(HandlerMethod handlerMethod) {
    // 检查方法和类上是否有SkipResponseWrapper注解
    if (handlerMethod.hasMethodAnnotation(SkipResponseWrapper.class) ||
        handlerMethod.getBeanType().isAnnotationPresent(SkipResponseWrapper.class)) {
      return false;
    }

    // 检查返回类型是否为ResponseEntity或Resource
    Class<?> returnType = handlerMethod.getReturnType().getParameterType();
    return !ResponseEntity.class.isAssignableFrom(returnType) &&
        !Resource.class.isAssignableFrom(returnType);
  }

  private Schema<?> createSuccessResponseSchema() {
    // 创建成功响应的统一 Schema
    return new Schema<>()
        .type("object")
        .addProperty("code", new IntegerSchema())
        .addProperty("message", new StringSchema())
        .addProperty("data", new Schema<>().type("object"))
        .addProperty("path", new StringSchema())
        .addProperty("method", new StringSchema())
        .addProperty("timestamp", new Schema<>().type("string").format("date-time"))
        .addProperty("correlationId", new StringSchema());
  }

  private Schema<?> createErrorResponseSchema(Schema<?> errorFieldSchema, Schema<?> errorInfoSchema) {
    // 创建错误响应的统一 Schema
    return new Schema<>()
        .type("object")
        .addProperty("code", new IntegerSchema())
        .addProperty("message", new StringSchema())
        .addProperty("data", new Schema<>().type("object"))
        .addProperty("error", errorInfoSchema)
        .addProperty("details", new ArraySchema().items(errorFieldSchema))
        .addProperty("path", new StringSchema())
        .addProperty("method", new StringSchema())
        .addProperty("timestamp", new Schema<>().type("string").format("date-time"))
        .addProperty("correlationId", new StringSchema());
  }

  private Schema<?> createErrorFieldSchema() {
    // 创建 ErrorField Schema
    return new Schema<>()
        .type("object")
        .addProperty("code", new StringSchema())
        .addProperty("message", new StringSchema());
  }

  private Schema<?> createErrorInfoSchema() {
    // 创建 ErrorInfo Schema
    return new Schema<>()
        .type("object")
        .addProperty("field", new StringSchema())
        .addProperty("message", new StringSchema())
        .addProperty("args", new ArraySchema().items(new Schema<>().type("object")));
  }

  private Example createOkExample() {
    // 创建成功响应示例
    return new Example()
        .value(Map.of(
            "code", HttpStatus.OK.value(),
            "message", "操作成功",
            "data", Map.of("example", "value"),
            "path", "/api/example",
            "method", "GET",
            "timestamp", DateTimeUtils.formatUtcTime(LocalDateTime.now()),
            "correlationId", "123e4567-e89b-12d3-a456-426614174000"
        ));
  }

  private Example createBadRequestExample() {
    // 创建错误响应示例
    return new Example()
        .value(Map.of(
            "code", HttpStatus.BAD_REQUEST.value(),
            "message", "请求参数错误",
            "error", Map.of(
                "code", "INVALID_PARAMETER",
                "message", "参数验证失败"
            ),
            "details", List.of(
                Map.of(
                    "field", "username",
                    "message", "用户名不能为空"
                )
            ),
            "path", "/api/example",
            "method", "POST",
            "timestamp", DateTimeUtils.formatUtcTime(LocalDateTime.now()),
            "correlationId", "123e4567-e89b-12d3-a456-426614174000"
        ));
  }
}
