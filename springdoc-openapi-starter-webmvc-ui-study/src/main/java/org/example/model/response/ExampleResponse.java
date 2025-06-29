package org.example.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "The response of example", example = "1")
public class ExampleResponse {

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "The field of BigDecimal", example = "1")
  private BigDecimal bigDecimalField;

  @Schema(description = "The field of String", example = "This is string field")
  private String stringField;

  @Schema(description = "The field of LocalDateTime", example = "2025-06-29T10:18:58.923Z", type = "string", format = "date-time")
  private LocalDateTime localDateTimeField;

  @Schema(description = "The field of Boolean", example = "true")
  private Boolean booleanField;
}
