package org.example.utils;

import java.math.BigDecimal;
import java.util.List;
import org.example.controller.ExampleController;
import org.example.model.ApiResponse.SuccessResponse;
import org.example.model.response.ExampleResponse;
import org.junit.jupiter.api.Test;

public class OpenAPIGeneratorTest {

  @Test
  public void testGenerateOpenAPIDoc() {
    String openApiJson = OpenAPIGenerator.generateOpenAPIDoc(
        ExampleController.class,
        "get",
        List.of(SuccessResponse.class, ExampleResponse.class),
        BigDecimal.class
    );
    System.out.println(openApiJson);
  }
}
