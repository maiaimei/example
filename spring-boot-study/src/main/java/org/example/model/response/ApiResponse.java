package org.example.model.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.constants.ErrorCode;
import org.example.constants.ResponseCode;
import org.example.utils.TraceIdUtils;

/**
 * ApiResponse.java
 * <p>
 * This class represents the API response structure for both success and error responses. It provides methods to create success and
 * error responses with various details.
 * </p>
 */
public class ApiResponse {

  public static <T> SuccessResponse<T> success(T data, String path, String method) {
    return SuccessResponse.<T>create()
        .data(data)
        .path(path)
        .method(method);
  }

  public static ErrorResponse<Object> error(ResponseCode responseCode, String path, String method) {
    return ErrorResponse.create()
        .code(responseCode.getCode())
        .message(responseCode.getMessage())
        .path(path)
        .method(method);
  }

  public static ErrorResponse<Object> error(ResponseCode responseCode, String path, String method, ErrorInfo error) {
    return ErrorResponse.create()
        .code(responseCode.getCode())
        .message(responseCode.getMessage())
        .error(error)
        .path(path)
        .method(method);
  }

  public static ErrorResponse<Object> error(ResponseCode responseCode, String path, String method, ErrorInfo error,
      List<FieldError> details) {
    return ErrorResponse.create()
        .code(responseCode.getCode())
        .message(responseCode.getMessage())
        .error(error)
        .details(details)
        .path(path)
        .method(method);
  }

  public static ErrorResponse<Object> error(ResponseCode responseCode, String path, String method,
      List<FieldError> details) {
    return ErrorResponse.create()
        .code(responseCode.getCode())
        .message(responseCode.getMessage())
        .details(details)
        .path(path)
        .method(method);
  }

  public static ErrorResponse<Object> error(ResponseCode responseCode, String path, String method, ErrorCode errorCode) {
    return ErrorResponse.create()
        .code(responseCode.getCode())
        .message(responseCode.getMessage())
        .error(ErrorInfo.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build())
        .path(path)
        .method(method);
  }

  public static ErrorResponse<Object> error(ResponseCode responseCode, String path, String method, ErrorCode errorCode,
      List<FieldError> details) {
    return ErrorResponse.create()
        .code(responseCode.getCode())
        .message(responseCode.getMessage())
        .error(ErrorInfo.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build())
        .details(details)
        .path(path)
        .method(method);
  }

  /**
   * 接口定义，表示基本的响应类型
   */
  public interface BasicResponse {

  }

  /**
   * 成功响应类，表示成功的响应
   *
   * @param <T> 响应数据类型
   */
  @Data
  @Accessors(chain = true)
  public static class SuccessResponse<T> implements BasicResponse {

    private Integer code;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String path;
    private String method;
    private String correlationId;

    // 链式调用方法
    public SuccessResponse<T> code(Integer code) {
      this.code = code;
      return this;
    }

    public SuccessResponse<T> message(String message) {
      this.message = message;
      return this;
    }

    public SuccessResponse<T> data(T data) {
      this.data = data;
      return this;
    }

    public SuccessResponse<T> timestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public SuccessResponse<T> path(String path) {
      this.path = path;
      return this;
    }

    public SuccessResponse<T> method(String method) {
      this.method = method;
      return this;
    }

    public SuccessResponse<T> correlationId(String correlationId) {
      this.correlationId = correlationId;
      return this;
    }

    // 静态工厂方法，创建实例
    public static <T> SuccessResponse<T> create() {
      return new SuccessResponse<T>()
          .code(ResponseCode.SUCCESS.getCode())
          .message(ResponseCode.SUCCESS.getMessage())
          .timestamp(LocalDateTime.now())
          .correlationId(TraceIdUtils.getTraceId());
    }
  }

  /**
   * 错误响应类，表示错误的响应
   *
   * @param <T> 响应数据类型
   */
  @Data
  @Accessors(chain = true)
  public static class ErrorResponse<T> implements BasicResponse {

    private Integer code;
    private String message;
    private T data;
    private ErrorInfo error;
    private List<FieldError> details;
    private LocalDateTime timestamp;
    private String path;
    private String method;
    private String correlationId;

    // 链式调用方法
    public ErrorResponse<T> code(Integer code) {
      this.code = code;
      return this;
    }

    public ErrorResponse<T> message(String message) {
      this.message = message;
      return this;
    }

    public ErrorResponse<T> data(T data) {
      this.data = data;
      return this;
    }

    public ErrorResponse<T> error(ErrorInfo error) {
      this.error = error;
      return this;
    }

    public ErrorResponse<T> details(List<FieldError> details) {
      this.details = details;
      return this;
    }

    public ErrorResponse<T> timestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public ErrorResponse<T> path(String path) {
      this.path = path;
      return this;
    }

    public ErrorResponse<T> method(String method) {
      this.method = method;
      return this;
    }

    public ErrorResponse<T> correlationId(String correlationId) {
      this.correlationId = correlationId;
      return this;
    }

    // 静态工厂方法，创建实例
    public static <T> ErrorResponse<T> create() {
      return new ErrorResponse<T>()
          .timestamp(LocalDateTime.now())
          .correlationId(TraceIdUtils.getTraceId());
    }
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ErrorInfo {

    private Integer code;
    private String message;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FieldError {

    private String field;
    private String message;
    private Object[] args;
  }
}
