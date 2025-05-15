package org.example.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "The request of example")
public class ExampleRequest {

  @Schema(description = "The field of BigDecimal", example = "1")
  private BigDecimal bigDecimalField;

  @NotBlank
  @Schema(description = "The field of String", example = "This is string field", requiredMode = RequiredMode.REQUIRED)
  private String stringField;

  @Schema(description = "The field of LocalDateTime", example = "2025-05-15T21:14:26.858234600")
  private LocalDateTime localDateTimeField;

  @Schema(description = "The field of Boolean", example = "true")
  private Boolean booleanField;

}
