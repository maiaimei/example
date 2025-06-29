package org.example.config;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.annotation.SkipResponseWrapper;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

  @Bean
  public OpenAPI openApi() {
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
      final Components components = openAPI.getComponents();
      final ModelConverters converters = ModelConverters.getInstance();

      // 创建统一响应的schema
      final Schema<?> errorFieldSchema = createErrorFieldSchema();
      final Schema<?> errorInfoSchema = createErrorInfoSchema();
      final Schema<?> errorResponseSchema = createErrorApiResponseSchema(errorFieldSchema, errorInfoSchema);

      // 添加统一响应schema到组件
      components.addSchemas("ErrorApiResponse", errorResponseSchema);
      components.addSchemas("ErrorInfo", errorInfoSchema);
      components.addSchemas("ErrorField", errorFieldSchema);

      // 遍历所有路径并处理操作
      openAPI.getPaths().forEach((path, pathItem) -> processPathItem(pathItem, components, converters));
    };
  }

  private void processPathItem(PathItem pathItem, Components components, ModelConverters converters) {
    Stream.of(pathItem.getGet(), pathItem.getPost(), pathItem.getPut(), pathItem.getDelete(), pathItem.getPatch())
        .filter(Objects::nonNull)
        .forEach(operation -> processOperation(operation, components, converters));
  }

  private void processOperation(Operation operation, Components components, ModelConverters converters) {
    String operationId = operation.getOperationId();
    if (Objects.isNull(operationId)) {
      return;
    }

    try {
      String[] parts = operationId.split("_");
      if (parts.length != 2) {
        return;
      }

      String className = parts[0];
      String methodName = parts[1];

      Class<?> controllerClass = Class.forName(className);
      Method method = Arrays.stream(controllerClass.getDeclaredMethods())
          .filter(m -> m.getName().equals(methodName))
          .findFirst()
          .orElse(null);

      if (Objects.isNull(method)) {
        return;
      }

      HandlerMethod handlerMethod = new HandlerMethod(applicationContext.getBean(controllerClass), method);

      if (shouldWrapResponse(handlerMethod)) {
        wrapResponse(operation, handlerMethod, components, converters);
      }
    } catch (Exception e) {
      log.warn("Failed to process operation: {}", operationId, e);
    }
  }

  private void wrapResponse(Operation operation, HandlerMethod handlerMethod, Components components,
      ModelConverters converters) {
    final RequestDetails requestDetails = extractRequestDetails(handlerMethod);
    ApiResponse originalResponse = operation.getResponses().computeIfAbsent("200", k -> new ApiResponse());

    if (Objects.isNull(originalResponse.getContent())) {
      originalResponse.setContent(new Content());
    }

    MediaType mediaType = originalResponse.getContent().get("application/json");
    if (Objects.isNull(mediaType)) {
      mediaType = new MediaType();
      originalResponse.getContent().addMediaType("application/json", mediaType);
    }

    Schema<?> originalSchema = Objects.nonNull(mediaType.getSchema())
        ? mediaType.getSchema()
        : inferSchemaFromMethod(handlerMethod, converters);

    Schema<?> successSchema = createSuccessSchema(originalSchema, requestDetails);
    String successSchemaName = StringUtils.hasText(originalSchema.getName())
        ? "SuccessApiResponse%s".formatted(originalSchema.getName())
        : "SuccessApiResponse";

    components.addSchemas(successSchemaName, successSchema);

    Content successContent = new Content()
        .addMediaType("application/json", new MediaType().schema(
            new Schema<>().type("object").$ref("#/components/schemas/%s".formatted(successSchemaName))));

    operation.getResponses().put("200", new ApiResponse()
        .description("Successful operation")
        .content(successContent));
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

  private Schema<?> createSuccessSchema(Schema<?> dataSchema, RequestDetails requestDetails) {
    // 创建成功响应的统一 Schema
    return new Schema<>()
        .type("object")
        .addProperty("code", new IntegerSchema()
            .description("HTTP status code indicating the result of the operation.")
            .example(HttpStatus.OK.value()))
        .addProperty("message", new StringSchema()
            .description("A brief message describing the result of the operation.")
            .example("success"))
        .addProperty("data", dataSchema
            .description("The actual data returned by the operation."))
        .addProperty("path", new StringSchema()
            .description("The request path that was accessed.")
            .example(requestDetails.requestPath))
        .addProperty("method", new StringSchema()
            .description("The HTTP method used for the request.")
            .example(requestDetails.requestMethod))
        .addProperty("timestamp", new DateTimeSchema()
            .description("The timestamp when the response was generated.")
            .example("2025-06-29T10:18:58.923Z"))
        .addProperty("correlationId", new StringSchema()
            .description("A unique identifier for tracing the request across systems.")
            .example("123e4567-e89b-12d3-a456-426614174000"));
  }

  private Schema<?> createErrorApiResponseSchema(Schema<?> errorFieldSchema, Schema<?> errorInfoSchema) {
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

  private Schema<?> inferSchemaFromMethod(HandlerMethod handlerMethod, ModelConverters converters) {
    // 获取方法返回类型
    Type returnType = handlerMethod.getMethod().getGenericReturnType();
    Class<?> rawType = handlerMethod.getMethod().getReturnType();

    try {
      if (returnType instanceof ParameterizedType parameterizedType) {
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

        // 处理集合类型
        if (Collection.class.isAssignableFrom(rawType)) {
          Map<String, Schema> itemSchemaMap = converters.read(actualTypeArguments[0]);
          Schema<?> itemSchema = !itemSchemaMap.isEmpty() ? itemSchemaMap.values().iterator().next()
              : new Schema<>().type("object");
          return new ArraySchema().name(itemSchema.getName() + rawType.getSimpleName()).items(itemSchema);
        }

        // 处理其他泛型类型
        Map<String, Schema> schemaMap = converters.read(returnType);
        return !schemaMap.isEmpty() ? schemaMap.values().iterator().next() : new Schema<>().type("object");
      }

      // 处理基本类型
      if (rawType.isPrimitive() || Number.class.isAssignableFrom(rawType)
          || rawType == String.class || rawType == Boolean.class) {
        return inferSchemaForPrimitiveType(rawType);
      }

      // 处理普通类型
      Map<String, Schema> schemaMap = converters.read(rawType);
      return !schemaMap.isEmpty() ? schemaMap.values().iterator().next() : new Schema<>().type("object");

    } catch (Exception e) {
      log.warn("Failed to generate schema for type: {}", returnType, e);
      return new Schema<>().type("object");
    }
  }

  private Schema<?> inferSchemaForPrimitiveType(Class<?> type) {
    if (type == String.class) {
      return new StringSchema();
    }
    if (type == Boolean.class || type == boolean.class) {
      return new BooleanSchema();
    }
    if (Number.class.isAssignableFrom(type) || type.isPrimitive()) {
      if (type == Integer.class || type == int.class) {
        return new IntegerSchema();
      }
      if (type == Long.class || type == long.class) {
        return new IntegerSchema().format("int64");
      }
      if (type == Float.class || type == float.class) {
        return new NumberSchema().format("float");
      }
      if (type == Double.class || type == double.class) {
        return new NumberSchema().format("double");
      }
      if (type == BigDecimal.class) {
        return new NumberSchema();
      }
    }
    return new Schema<>().type("object");
  }

  private RequestDetails extractRequestDetails(HandlerMethod handlerMethod) {
    // 获取类上的 @RequestMapping 注解
    RequestMapping classRequestMapping = handlerMethod.getBeanType().getAnnotation(RequestMapping.class);
    String[] classPaths = Objects.nonNull(classRequestMapping) ? classRequestMapping.value() : new String[]{};

    // 获取方法上的 @RequestMapping 注解
    RequestMapping methodRequestMapping = handlerMethod.getMethodAnnotation(RequestMapping.class);
    String[] methodPaths = Objects.nonNull(methodRequestMapping) ? methodRequestMapping.value() : new String[]{};

    // 合并类路径和方法路径，确保数组不为空
    String classPath = classPaths.length > 0 ? classPaths[0] : "";
    String methodPath = methodPaths.length > 0 ? methodPaths[0] : "";
    String requestPath = StringUtils.cleanPath("%s/%s".formatted(classPath, methodPath));

    // 获取请求方法（GET, POST, etc.），确保数组不为空
    RequestMethod[] requestMethods = Objects.nonNull(methodRequestMapping) ? methodRequestMapping.method()
        : new RequestMethod[]{};
    String requestMethod = requestMethods.length > 0 ? requestMethods[0].name() : "";

    return new RequestDetails(requestPath, requestMethod);
  }

  @Data
  @AllArgsConstructor
  private static class RequestDetails {

    private String requestPath;
    private String requestMethod;
  }
}
