package org.example.model;

import java.time.LocalDateTime;

public class ErrorResponse {

  private Integer code;
  private String message;
  private String error;
  private String details;
  private LocalDateTime timestamp;
  private String path;
  private String method;
  private String correlationId;
}
