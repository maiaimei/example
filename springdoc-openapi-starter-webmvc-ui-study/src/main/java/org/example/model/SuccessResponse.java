package org.example.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SuccessResponse<T> {

  private Integer code;
  private String message;
  private T data;
  private LocalDateTime timestamp;
  private String path;
  private String method;
  private String correlationId;
}
