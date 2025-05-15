package org.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "The request wrapper")
public class ApiRequest<T> {

  @Schema(description = "The data of request")
  private T data;
}
