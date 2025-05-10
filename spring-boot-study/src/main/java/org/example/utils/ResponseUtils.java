package org.example.utils;

import org.example.constants.ResponseCode;
import org.example.model.response.ApiError;
import org.example.model.response.ApiErrorDetail;
import org.example.model.response.ApiErrorResponse;
import org.example.model.response.ApiResponse;

import java.math.BigDecimal;
import java.util.List;

public final class ResponseUtils {

  private ResponseUtils() {
    throw new UnsupportedOperationException();
  }

  // 成功响应 - 基本成功
  public static <T> ApiResponse<T> success(T data) {
    ApiResponse<T> result = new ApiResponse<>();
    result.setCode(ResponseCode.SUCCESS.getCode());
    result.setMessageKey(ResponseCode.SUCCESS.getMessageKey());
    result.setData(data);
    result.setTimestamp(System.currentTimeMillis());
    return result;
  }

  // 成功响应 - 带路径和追踪ID
  public static <T> ApiResponse<T> success(T data, String path, BigDecimal traceId) {
    ApiResponse<T> result = success(data);
    result.setPath(path);
    result.setTraceId(traceId);
    return result;
  }

  // 错误响应 - 基本错误
  public static <T> ApiErrorResponse<T> error(ResponseCode responseCode) {
    ApiErrorResponse<T> result = new ApiErrorResponse<>();
    result.setCode(responseCode.getCode());
    result.setMessageKey(responseCode.getMessageKey());
    result.setTimestamp(System.currentTimeMillis());
    return result;
  }

  // 错误响应 - 带错误详情
  public static <T> ApiErrorResponse<T> error(ResponseCode responseCode, ApiError error) {
    ApiErrorResponse<T> result = error(responseCode);
    result.setError(error);
    return result;
  }

  // 错误响应 - 带错误详情和路径
  public static <T> ApiErrorResponse<T> error(ResponseCode responseCode, ApiError error, String path) {
    ApiErrorResponse<T> result = error(responseCode, error);
    result.setPath(path);
    return result;
  }

  // 错误响应 - 带错误详情、路径和追踪ID
  public static <T> ApiErrorResponse<T> error(ResponseCode responseCode, ApiError error, String path,
      BigDecimal traceId) {
    ApiErrorResponse<T> result = error(responseCode, error, path);
    result.setTraceId(traceId);
    return result;
  }

  // 错误响应 - 带错误详情列表
  public static <T> ApiErrorResponse<T> error(ResponseCode responseCode, ApiError error,
      List<ApiErrorDetail> details) {
    ApiErrorResponse<T> result = error(responseCode, error);
    result.setDetails(details);
    return result;
  }

  // 错误响应 - 带完整信息
  public static <T> ApiErrorResponse<T> error(ResponseCode responseCode, ApiError error,
      List<ApiErrorDetail> details, String path, BigDecimal traceId) {
    ApiErrorResponse<T> result = error(responseCode, error, details);
    result.setPath(path);
    result.setTraceId(traceId);
    return result;
  }
}
