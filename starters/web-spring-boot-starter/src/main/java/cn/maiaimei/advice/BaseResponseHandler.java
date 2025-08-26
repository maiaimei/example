package cn.maiaimei.advice;

import cn.maiaimei.annotation.SkipResponseWrapper;
import cn.maiaimei.model.ApiResponse;
import cn.maiaimei.model.response.BaseApiResponse;
import cn.maiaimei.util.JsonUtils;
import cn.maiaimei.util.RequestUtils;
import java.lang.reflect.Method;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * BaseResponseHandler
 * <p>
 * This class is a global response handler that intercepts and processes responses from controllers. It wraps the response body into
 * a standardized API response structure unless explicitly excluded.
 * </p>
 */
@Slf4j
public abstract class BaseResponseHandler implements ResponseBodyAdvice<Object> {

  /**
   * Determines whether this advice is applicable for the given method return type and converter type.
   *
   * @param returnType    The method return type.
   * @param converterType The converter type.
   * @return true if the advice should be applied, false otherwise.
   */
  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    // Exclude certain types and methods annotated with SkipResponseWrapper
    return !isExcludedType(returnType) && !hasSkipResponseWrapperAnnotation(returnType);
  }

  /**
   * Checks if the given return type is excluded from processing.
   *
   * @param returnType The method return type.
   * @return true if the type is excluded, false otherwise.
   */
  private boolean isExcludedType(MethodParameter returnType) {
    // Exclude specific types such as ResponseEntity and Resource
    Class<?> parameterType = returnType.getParameterType();
    return ResponseEntity.class.isAssignableFrom(parameterType) ||
        Resource.class.isAssignableFrom(parameterType);
  }

  /**
   * Checks if the method or class has the SkipResponseWrapper annotation.
   *
   * @param returnType The method return type.
   * @return true if the annotation is present, false otherwise.
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

  /**
   * Processes the response body before it is written to the HTTP response.
   *
   * @param body                  The response body.
   * @param returnType            The method return type.
   * @param selectedContentType   The selected content type.
   * @param selectedConverterType The selected converter type.
   * @param request               The server HTTP request.
   * @param response              The server HTTP response.
   * @return The processed response body.
   */
  @Override
  public Object beforeBodyWrite(Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {
    String requestPath = RequestUtils.getUnifiedPath(request);
    String requestMethod = request.getMethod().name();
    try {
      // If the response body is null, return a success response with null data
      if (Objects.isNull(body)) {
        return ApiResponse.success(body, requestPath, requestMethod);
      }

      // If the response body is a String, convert it to JSON and wrap it in a success response
      if (body instanceof String) {
        return JsonUtils.toJson(ApiResponse.success(body, requestPath, requestMethod));
      }

      // If the response body is already a BaseApiResponse, return it as is
      if (body instanceof BaseApiResponse) {
        return body;
      }

      // Wrap other types of response bodies in a success response
      return ApiResponse.success(body, requestPath, requestMethod);
    } catch (Exception e) {
      // Log errors and return an error response
      log.error("Error processing response body for URI: {}, Method: {}", requestPath, requestMethod, e);
      return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, requestPath, requestMethod);
    }
  }

}