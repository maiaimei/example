package org.example.advice;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.example.annotation.SkipResponseWrapper;
import org.example.constants.ResponseCode;
import org.example.model.response.ApiResponse;
import org.example.model.response.ApiResponse.BasicResponse;
import org.example.utils.JsonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindingResult;
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
    return !shouldSkip(returnType);
  }

  /**
   * 排除不需要处理的返回类型
   */
  private boolean shouldSkip(MethodParameter returnType) {
    // 获取方法和类
    Method method = returnType.getMethod();
    if (method == null) {
      return false;
    }
    Class<?> declaringClass = returnType.getDeclaringClass();

    // 检查是否有跳过包装的注解
    if (method.isAnnotationPresent(SkipResponseWrapper.class) ||
        declaringClass.isAnnotationPresent(SkipResponseWrapper.class)) {
      return true; // 跳过包装
    }

    // 不处理以下类型
    Class<?> parameterType = returnType.getParameterType();
    return isExcludedType(parameterType);
  }

  /**
   * 检查是否为排除的类型
   */
  private boolean isExcludedType(Class<?> clazz) {
    return ResponseEntity.class.isAssignableFrom(clazz) ||
        Resource.class.isAssignableFrom(clazz) ||
        BindingResult.class.isAssignableFrom(clazz);
  }

  @Override
  public Object beforeBodyWrite(Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {
    try {
      // 处理空值
      if (body == null) {
        return handleNullResponse(selectedContentType, request, response);
      }

      // 处理文件下载
      if (isFileDownload(body, selectedContentType)) {
        return body;
      }

      // 处理SSE响应
      if (isSSEResponse(selectedContentType)) {
        return body;
      }

      // 处理String类型
      if (body instanceof String) {
        return handleStringResponse(body, selectedContentType, request, response);
      }

      // 处理BasicResponse类型
      if (body instanceof BasicResponse basicResponse) {
        return basicResponse;
      }

      // 其他类型统一包装
      return ApiResponse.success(body, request.getURI().getPath(), request.getMethod().name());
    } catch (Exception e) {
      log.error("Error processing response body", e);
      return ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR, request.getURI().getPath(), request.getMethod().name());
    }
  }

  /**
   * 处理空值返回
   */
  private Object handleNullResponse(MediaType selectedContentType, ServerHttpRequest request, ServerHttpResponse response) {
    if (MediaType.APPLICATION_JSON.includes(selectedContentType)) {
      response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
      return JsonUtils.toJsonString(ApiResponse.success(null, request.getURI().getPath(), request.getMethod().name()));
    }
    response.setStatusCode(HttpStatus.NO_CONTENT);
    return null;
  }

  /**
   * 处理String类型响应
   */
  private Object handleStringResponse(Object body, MediaType selectedContentType, ServerHttpRequest request,
      ServerHttpResponse response) {
    // 如果客户端期望JSON
    if (MediaType.APPLICATION_JSON.includes(selectedContentType)) {
      response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
      return JsonUtils.toJsonString(ApiResponse.success(body, request.getURI().getPath(), request.getMethod().name()));
    }
    // 如果是普通文本
    return body;
  }

  /**
   * 判断是否为文件下载请求
   */
  private boolean isFileDownload(Object body, MediaType selectedContentType) {
    return body instanceof Resource || body instanceof byte[] ||
        (selectedContentType != null && selectedContentType.equals(MediaType.APPLICATION_OCTET_STREAM));
  }

  /**
   * 检查是否为SSE响应
   */
  private boolean isSSEResponse(MediaType mediaType) {
    return mediaType != null && (mediaType.includes(MediaType.TEXT_EVENT_STREAM) ||
        mediaType.includes(MediaType.APPLICATION_NDJSON));
  }

}
