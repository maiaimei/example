package org.example.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "The request of example page query")
public class ExamplePageQueryRequest {

  @Schema(description = "The field of String", example = "This is string field")
  private String stringField;
}
