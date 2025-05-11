package org.example.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.constants.ErrorCode;
import org.example.constants.ResponseCode;
import org.example.exception.BusinessException;
import org.example.model.response.ApiResponse;
import org.example.model.response.ApiResponse.BasicResponse;
import org.example.model.response.ApiResponse.FieldError;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final MessageSource fieldMessageSource;

  private final MessageSource validationMessageSource;

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public BasicResponse handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
    return ApiResponse.error(ResponseCode.NOT_FOUND, request.getPathInfo(), request.getMethod());
  }

  /**
   * Handle HTTP message not readable exception
   * <p> HttpMessageNotReadableException 是 Spring 框架中的一个重要异常，主要负责处理 HTTP 请求体解析失败的情况。</p>
   * <p> 请求体缺失 </p>
   * <p> 请求体解析失败 </p>
   * <p> 数据类型转换错误 </p>
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BasicResponse handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex,
      HttpServletRequest request) {
    log.warn("Message parsing error - URL: {} - Error: {}",
        request.getRequestURL().toString(),
        ex.getMostSpecificCause().getMessage());

    return ApiResponse.error(ResponseCode.BAD_REQUEST, request.getPathInfo(), request.getMethod(), ErrorCode.MESSAGE_PARSE_ERROR);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BasicResponse handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex, HttpServletRequest request) {
    log.warn("Missing parameter - URL: {} - Parameter: {} - Type: {}",
        request.getRequestURL().toString(),
        ex.getParameterName(),
        ex.getParameterType());

    List<FieldError> details = new ArrayList<>();
    FieldError detail = new FieldError();
    detail.setField(ex.getParameterName());

    return ApiResponse.error(ResponseCode.BAD_REQUEST, request.getPathInfo(), request.getMethod(), ErrorCode.PARAM_MISSING,
        details);
  }

  /**
   * 处理参数验证异常
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BasicResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    log.warn("Validation failed - URL: {} - Errors: {}",
        request.getRequestURL().toString(),
        ex.getBindingResult().getFieldErrors());

    List<FieldError> details = new ArrayList<>();
    ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
      String fieldToUse = getField(fieldError.getField());
      FieldError detail = new FieldError();
      detail.setField(fieldToUse);
      detail.setMessage(fieldError.getDefaultMessage());
      details.add(detail);
    });

    return ApiResponse.error(ResponseCode.BAD_REQUEST, request.getPathInfo(), request.getMethod(), ErrorCode.VALIDATION_FAILED,
        details);
  }

  /**
   * 处理参数绑定异常
   */
  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BasicResponse handleBindException(BindException ex, HttpServletRequest request) {
    List<FieldError> details = new ArrayList<>();
    ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
      String field = fieldError.getField();
      String defaultMessage = fieldError.getDefaultMessage();
      FieldError detail = new FieldError();
      detail.setField(field);
      detail.setMessage(defaultMessage);
      details.add(detail);
    });

    return ApiResponse.error(ResponseCode.BAD_REQUEST, request.getPathInfo(), request.getMethod(), details);
  }

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BasicResponse handleValidationException(ValidationException ex, HttpServletRequest request) {
    List<FieldError> details = new ArrayList<>();
    return ApiResponse.error(ResponseCode.BAD_REQUEST, request.getPathInfo(), request.getMethod(), details);
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public BasicResponse handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
    return ApiResponse.error(ResponseCode.FORBIDDEN, request.getPathInfo(), request.getMethod());
  }

  /**
   * 处理业务异常
   */
  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public BasicResponse handleBusinessException(BusinessException ex, HttpServletRequest request) {
    return ApiResponse.error(ex.getResponseCode(), request.getPathInfo(), request.getMethod());
  }

  /**
   * 处理其他未知异常
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public BasicResponse handleException(Exception ex, HttpServletRequest request) {
    log.error("Unexpected error occurred", ex);
    return ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR, request.getPathInfo(), request.getMethod());
  }

  /**
   * 获取字段名称
   *
   * @param field 字段键
   * @return 字段名称
   */
  private String getField(String field) {
    String fieldToUse = field;
    try {
      fieldToUse = fieldMessageSource.getMessage(field, null, null);
    } catch (NoSuchMessageException e) {
      log.warn("Field message not found for key: {}", field);
    }
    return fieldToUse;
  }

}
