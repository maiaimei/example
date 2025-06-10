package org.example.model;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.example.utils.RequestUtils;
import org.example.utils.TraceIdUtils;
import org.springframework.http.HttpStatus;

/**
 * ApiResponse.java
 * <p>
 * This class represents the API response structure for both success and error responses. It provides methods to create success and
 * error responses with various details.
 * </p>
 */
public class ApiResponse {

  public static <T> SuccessApiResponse<T> success(T data, String path, String method) {
    final HttpStatus httpStatus = HttpStatus.OK;
    return SuccessApiResponse.<T>builder()
        .code(httpStatus.value())
        .message(httpStatus.getReasonPhrase())
        .data(data)
        .path(path)
        .method(method)
        .timestamp(LocalDateTime.now())
        .correlationId(TraceIdUtils.getTraceId())
        .build();
  }

  public static <T> ErrorApiResponse<T> error(HttpStatus httpStatus, HttpServletRequest request) {
    return error(httpStatus, RequestUtils.getUnifiedPath(request), request.getMethod());
  }

  public static <T> ErrorApiResponse<T> error(HttpStatus httpStatus, String path, String method) {
    return error(httpStatus, null, path, method);
  }

  public static <T> ErrorApiResponse<T> error(HttpStatus httpStatus, T data, String path, String method) {
    return ErrorApiResponse.<T>builder()
        .code(httpStatus.value())
        .message(httpStatus.getReasonPhrase())
        .data(data)
        .path(path)
        .method(method)
        .timestamp(LocalDateTime.now())
        .correlationId(TraceIdUtils.getTraceId())
        .build();
  }

}
