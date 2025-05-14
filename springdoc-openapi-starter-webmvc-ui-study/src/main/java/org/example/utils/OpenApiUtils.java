package org.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.api.OpenApiResource;

@Slf4j
public class OpenApiUtils {

  public static void printOpenApi(OpenAPI openAPI) {
    try {
      String json = writeJsonValue(openAPI);
      log.info("Generated OpenAPI Documentation:\n{}", json);
    } catch (Exception e) {
      log.error("Failed to print OpenAPI documentation", e);
    }
  }

  public static void saveOpenApiToFile(OpenAPI openAPI, String fileName) {
    try {
      String content = writeJsonValue(openAPI);
      Path path = Paths.get(fileName);
      Files.writeString(path, content);
      log.info("OpenAPI documentation has been saved to: {}", path.toAbsolutePath());
    } catch (IOException e) {
      log.error("Failed to save OpenAPI documentation to file", e);
    }
  }

  /**
   * {@link OpenApiResource#openapiJson(HttpServletRequest, String, Locale)}
   * {@link OpenApiResource#openapiYaml(HttpServletRequest, String, Locale)}
   * {@link ObjectMapperProvider#ObjectMapperProvider(SpringDocConfigProperties)}
   */
  private static String writeJsonValue(OpenAPI openAPI) throws JsonProcessingException {
    ObjectMapper objectMapper = Json31.mapper();
    return objectMapper.writerWithDefaultPrettyPrinter().forType(OpenAPI.class).writeValueAsString(openAPI);
  }

}
