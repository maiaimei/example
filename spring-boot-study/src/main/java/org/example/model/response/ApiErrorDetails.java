package org.example.model.response;

import lombok.Data;

@Data
public class ApiErrorDetails {
  private String field;
  private String message;
}
