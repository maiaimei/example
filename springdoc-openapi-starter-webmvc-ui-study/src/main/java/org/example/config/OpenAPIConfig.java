package org.example.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.example.openapi.OpenAPIAdvancedCustomizer;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link OpenApiResource#openapiJson(HttpServletRequest, String, Locale)}
 * {@link OpenApiResource#openapiYaml(HttpServletRequest, String, Locale)}
 * {@link ObjectMapperProvider#ObjectMapperProvider(SpringDocConfigProperties)}
 */
@Configuration
public class OpenAPIConfig {

  @Autowired
  private OpenAPIAdvancedCustomizer openApiAdvancedCustomizer;

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
      openApiAdvancedCustomizer.customise(openAPI);
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
