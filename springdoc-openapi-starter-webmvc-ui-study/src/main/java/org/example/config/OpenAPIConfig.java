package org.example.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;
import org.example.utils.OpenAPIModelSchemaGenerator;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link OpenApiResource#openapiJson(HttpServletRequest, String, Locale)}
 * {@link OpenApiResource#openapiYaml(HttpServletRequest, String, Locale)}
 * {@link ObjectMapperProvider#ObjectMapperProvider(SpringDocConfigProperties)}
 */
@Configuration
public class OpenAPIConfig {

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
  public OpenApiCustomizer openApiCustomizer() {
    return openAPI -> {

      OpenAPIModelSchemaGenerator generator = new OpenAPIModelSchemaGenerator(openAPI);
      generator.generateSchemas("org.example.model"); // 指定要扫描的包路径
      final Map<Class<?>, Object> examples = generator.getExamples();

      final Paths paths = openAPI.getPaths();
      paths.forEach((endpoint, pathItem) -> {
        // 遍历所有HTTP方法操作
        pathItem.readOperations().forEach(operation -> {
          // 添加成功响应 (200)
          ApiResponse successResponse = new ApiResponse()
              .description("操作成功")
              .content(new Content()
                  .addMediaType("application/json",
                      new MediaType().schema(
                          new Schema<>()
                              .type("object")
                              .addProperties("code", new Schema<>().type("integer").example(200))
                              .addProperties("message", new Schema<>().type("string").example("操作成功"))
                              .addProperties("data", new Schema<>().type("object")))));

          // 添加客户端错误响应 (400)
          ApiResponse clientErrorResponse = new ApiResponse()
              .description("客户端请求错误")
              .content(new Content()
                  .addMediaType("application/json",
                      new MediaType().schema(
                          new Schema<>()
                              .type("object")
                              .addProperties("code", new Schema<>().type("integer").example(400))
                              .addProperties("message", new Schema<>().type("string").example("请求参数错误"))
                              .addProperties("errors", new Schema<>().type("array").items(
                                  new Schema<>()
                                      .type("string"))))));

          // 添加服务器错误响应 (500)
          ApiResponse serverErrorResponse = new ApiResponse()
              .description("服务器内部错误")
              .content(new Content()
                  .addMediaType("application/json",
                      new MediaType().schema(
                          new Schema<>()
                              .type("object")
                              .addProperties("code", new Schema<>().type("integer").example(500))
                              .addProperties("message", new Schema<>().type("string").example("服务器内部错误")))));

          // 将响应添加到操作中
          operation.getResponses()
              .addApiResponse("200", successResponse)
              .addApiResponse("400", clientErrorResponse)
              .addApiResponse("500", serverErrorResponse);
        });
      });
    };
  }

  @Bean
  public GroupedOpenApi openApi30Docs() {
    return GroupedOpenApi.builder()
        .group("v3.0")
        .displayName("OpenAPI 3.0")
        .pathsToMatch("/**")
        .packagesToScan("org.example")
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
}
