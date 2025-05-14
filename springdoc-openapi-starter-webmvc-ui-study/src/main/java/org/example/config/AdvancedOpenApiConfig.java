package org.example.config;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.example.utils.OpenApiUtils;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AdvancedOpenApiConfig {

  private static final String CONFIG_FILE = "openapi/security-openapi-config.json";

  @Bean
  public OpenApiCustomizer openApiCustomizer() {
    return openApi -> {
      JsonNode config = OpenApiUtils.loadConfiguration(CONFIG_FILE);
      OpenApiUtils.configureOpenApiPathsOnly(openApi, config);
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
