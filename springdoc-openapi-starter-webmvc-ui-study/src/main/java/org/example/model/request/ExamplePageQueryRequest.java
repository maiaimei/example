package org.example.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.example.model.PageQueryRequest;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "The request of example page query")
public class ExamplePageQueryRequest extends PageQueryRequest {

  @Schema(description = "The field of String", example = "This is string field")
  private String stringField;
}
