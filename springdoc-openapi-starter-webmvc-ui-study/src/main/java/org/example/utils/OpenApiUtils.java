package org.example.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.models.OpenAPI;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenApiUtils {

  private static final ObjectMapper MAPPER = new ObjectMapper()
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .enable(SerializationFeature.INDENT_OUTPUT)
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  public static void printOpenApi(OpenAPI openAPI) {
    try {
      String json = MAPPER.writeValueAsString(openAPI);
      log.info("Generated OpenAPI Documentation:\n{}", json);
    } catch (Exception e) {
      log.error("Failed to print OpenAPI documentation", e);
    }
  }

  public static void saveOpenApiToFile(OpenAPI openAPI, String fileName) {
    try {
      String content = MAPPER.writeValueAsString(openAPI);
      Path path = Paths.get(fileName);
      Files.writeString(path, content);
      log.info("OpenAPI documentation has been saved to: {}", path.toAbsolutePath());
    } catch (IOException e) {
      log.error("Failed to save OpenAPI documentation to file", e);
    }
  }
}
