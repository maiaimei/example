package org.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
@Schema(description = "The response of page query")
public class Page<T> {

  @Schema(description = "The total numbers of records", example = "100")
  private long total;

  @Schema(description = "The page size", example = "10")
  private long size;

  @Schema(description = "The current page", example = "1")
  private long current;

  @Schema(description = "The total number of pages", example = "10")
  private long pages;

  @Schema(description = "The records of current page")
  private List<T> records;
}
