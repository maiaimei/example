package org.example.openapi;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import org.example.model.request.ExamplePageQueryRequest;
import org.example.model.request.ExampleRequest;
import org.junit.jupiter.api.Test;

public class OpenAPITest {

  @Test
  public void modelToSchema() {
    ModelConverters converters = ModelConverters.getInstance();
    List<Type> types = List.of(ExampleRequest.class, ExamplePageQueryRequest.class);
    for (Type type : types) {
      Map<String, Schema> schemas = converters.read(type);
      schemas.forEach((key, schema) -> {
        System.out.println("Schema for " + key + ":");
        System.out.println(Json.pretty(schema));
        System.out.println("-------------------");
      });
    }

  }
}
