package org.example.model.response;

import lombok.Data;

@Data
public class ApiError {

  private String type;
  private String message;
}
