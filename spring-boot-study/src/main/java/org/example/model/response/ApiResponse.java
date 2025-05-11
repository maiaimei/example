package org.example.model.response;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.constants.ResponseCode;
import org.example.utils.DateTimeUtils;
import org.example.utils.TraceIdUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

  private Integer code;
  private String message;
  private T data;
  private LocalDateTime timestamp;
  private String path;
  private String method;
  private String correlationId;

  public static <T> ApiResponse<T> success(T data, HttpServletRequest request) {
    return ApiResponse.<T>builder()
        .code(ResponseCode.SUCCESS.getCode())
        .message(ResponseCode.SUCCESS.getMessage())
        .data(data)
        .timestamp(DateTimeUtils.getCurrentUtcTime())
        .path(request.getRequestURI())
        .method(request.getMethod())
        .correlationId(TraceIdUtils.getTraceId())
        .build();
  }
}
