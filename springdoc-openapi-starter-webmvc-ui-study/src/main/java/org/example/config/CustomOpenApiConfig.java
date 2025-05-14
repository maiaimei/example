package org.example.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

//@Configuration
public class CustomOpenApiConfig {

  @Bean
  public OpenApiCustomizer openApiCustomizer() {
    return openApi -> {
      // 配置基本信息
      openApi.info(new Info()
          .title("Security API Documentation")
          .version("1.0.0")
          .description("API documentation for security endpoints"));

      // 获取所有路径
      Paths paths = openApi.getPaths();

      // 配置/security/login接口
      PathItem loginPath = paths.get("/security/login");
      if (loginPath != null) {
        Operation postOperation = loginPath.getPost();
        if (postOperation != null) {
          // 配置请求体Schema
          Schema<?> loginRequestSchema = new Schema<>()
              .type("object")
              .addProperty("username", new StringSchema())
              .addProperty("password", new StringSchema());

          Schema<?> apiRequestSchema = new Schema<>()
              .type("object")
              .addProperty("data", loginRequestSchema);

          // 配置响应体Schema
          Schema<?> jwtResponseSchema = new Schema<>()
              .type("object")
              .addProperty("accessToken", new StringSchema());

          Schema<?> successResponseSchema = new Schema<>()
              .type("object")
              .addProperty("code", new IntegerSchema())
              .addProperty("message", new StringSchema())
              .addProperty("data", jwtResponseSchema)
              .addProperty("timestamp", new DateTimeSchema())
              .addProperty("path", new StringSchema())
              .addProperty("method", new StringSchema())
              .addProperty("correlationId", new StringSchema());

          // 设置请求体
          postOperation.requestBody(new RequestBody()
              .content(new Content()
                  .addMediaType("application/json",
                      new MediaType().schema(apiRequestSchema))));

          // 设置响应体
          postOperation.responses(new ApiResponses()
              .addApiResponse("200", new ApiResponse()
                  .description("Successful operation")
                  .content(new Content()
                      .addMediaType("application/json",
                          new MediaType().schema(successResponseSchema)))));
        }
      }
    };
  }

  @Bean
  public GroupedOpenApi securityApi() {
    return GroupedOpenApi.builder()
        .group("security")
        .pathsToMatch("/security/**")
        .build();
  }
}
