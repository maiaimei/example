package org.example.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApiRequest<T> {

  @Valid
  @NotNull
  private T data;
}
