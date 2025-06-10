package org.example.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApiRequest<T> {

  @Valid
  @NotNull
  private T data;
}
