package org.example.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import org.example.controller.ExampleController;
import org.junit.jupiter.api.Test;

public class OpenAPIGeneratorTest {

  @Test
  public void generateAPI() {
    final OpenAPIGenerator openAPIGenerator = new OpenAPIGenerator();
    final OpenAPI openAPI = openAPIGenerator.generateAPI(ExampleController.class, "create");
    openAPIGenerator.printOpenAPI(openAPI);
  }
}
