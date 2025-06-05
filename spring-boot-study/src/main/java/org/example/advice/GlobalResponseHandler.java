package org.example.advice;

import java.lang.reflect.Method;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.example.annotation.SkipResponseWrapper;
import org.example.constants.ResponseCode;
import org.example.model.response.ApiResponse;
import org.example.model.response.ApiResponse.BasicResponse;
import org.example.utils.JsonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局响应处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    // 排除不需要处理的返回类型
    return !isExcludedType(returnType) && !hasSkipResponseWrapperAnnotation(returnType);
  }

  /**
   * 排除不需要处理的返回类型
   */
  private boolean isExcludedType(MethodParameter returnType) {
    // 不处理以下类型
    Class<?> parameterType = returnType.getParameterType();
    return ResponseEntity.class.isAssignableFrom(parameterType) ||
        Resource.class.isAssignableFrom(parameterType);
  }

  /**
   * 检查是否有跳过包装的注解
   */
  private boolean hasSkipResponseWrapperAnnotation(MethodParameter returnType) {
    Method method = returnType.getMethod();
    if (Objects.isNull(method)) {
      return false;
    }
    Class<?> declaringClass = returnType.getDeclaringClass();
    return method.isAnnotationPresent(SkipResponseWrapper.class) ||
        declaringClass.isAnnotationPresent(SkipResponseWrapper.class);
  }


  @Override
  public Object beforeBodyWrite(Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {
    try {
      return processResponseBody(body, selectedContentType, request, response);
    } catch (Exception e) {
      return handleException(e, request);
    }
  }

  /**
   * 处理响应体
   */
  private Object processResponseBody(Object body, MediaType selectedContentType,
      ServerHttpRequest request, ServerHttpResponse response) {
    String requestPath = request.getURI().getPath();
    String requestMethod = request.getMethod().name();

    if (Objects.isNull(body)) {
      return ApiResponse.success(body, requestPath, requestMethod);
    }

    if (body instanceof String) {
      return JsonUtils.toJsonString(ApiResponse.success(body, requestPath, requestMethod));
    }

    if (body instanceof BasicResponse) {
      return body;
    }

    return ApiResponse.success(body, requestPath, requestMethod);
  }

  /**
   * 统一异常处理
   */
  private Object handleException(Exception e, ServerHttpRequest request) {
    String requestPath = request.getURI().getPath();
    String requestMethod = request.getMethod().name();

    log.error("Error processing response body for URI: {}, Method: {}", requestPath, requestMethod, e);
    return ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR, requestPath, requestMethod);
  }

}
