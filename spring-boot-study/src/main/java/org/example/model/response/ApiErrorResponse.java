package org.example.model.response;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
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
public class ApiErrorResponse {

  private Integer code;
  private String message;
  private ErrorInfo error;
  private List<FieldError> details;
  private LocalDateTime timestamp;
  private String path;
  private String method;
  private String traceId;

  public static ApiErrorResponse error(ResponseCode responseCode, HttpServletRequest request) {
    return ApiErrorResponse.builder()
        .code(responseCode.getCode())
        .message(responseCode.getMessage())
        .timestamp(DateTimeUtils.getCurrentUtcTime())
        .path(request.getRequestURI())
        .method(request.getMethod())
        .traceId(TraceIdUtils.getTraceId())
        .build();
  }

  public static ApiErrorResponse error(ResponseCode responseCode, HttpServletRequest request, ErrorInfo error) {
    return ApiErrorResponse.builder()
        .code(responseCode.getCode())
        .message(responseCode.getMessage())
        .error(error)
        .timestamp(DateTimeUtils.getCurrentUtcTime())
        .path(request.getRequestURI())
        .method(request.getMethod())
        .traceId(TraceIdUtils.getTraceId())
        .build();
  }

  public static ApiErrorResponse error(ResponseCode responseCode, HttpServletRequest request, ErrorInfo error,
      List<FieldError> details) {
    return ApiErrorResponse.builder()
        .code(responseCode.getCode())
        .message(responseCode.getMessage())
        .error(error)
        .details(details)
        .timestamp(DateTimeUtils.getCurrentUtcTime())
        .path(request.getRequestURI())
        .method(request.getMethod())
        .traceId(TraceIdUtils.getTraceId())
        .build();
  }

  public static ApiErrorResponse error(ResponseCode responseCode, HttpServletRequest request, List<FieldError> details) {
    return ApiErrorResponse.builder()
        .code(responseCode.getCode())
        .message(responseCode.getMessage())
        .details(details)
        .timestamp(DateTimeUtils.getCurrentUtcTime())
        .path(request.getRequestURI())
        .method(request.getMethod())
        .traceId(TraceIdUtils.getTraceId())
        .build();
  }

}
