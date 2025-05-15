package org.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "The request of page query")
public class PageQueryRequest {

  @Min(1)
  @Schema(description = "The current page", example = "1")
  private long current;

  @Max(100)
  @Schema(description = "The page size", example = "10")
  private long size;
}
