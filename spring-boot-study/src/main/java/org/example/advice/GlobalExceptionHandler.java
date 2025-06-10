package org.example.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.constant.ErrorCode;
import org.example.exception.BusinessException;
import org.example.exception.SystemException;
import org.example.model.ApiResponse;
import org.example.model.ErrorApiResponse;
import org.example.model.ErrorField;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * GlobalExceptionHandler
 * <p>
 * This class handles exceptions globally for the application and provides standardized error responses.
 * </p>
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final MessageSource fieldMessageSource;

  private final MessageSource validationMessageSource;

  /**
   * Handles resource not found exceptions.
   *
   * @param ex      NoResourceFoundException
   * @param request HttpServletRequest
   * @return ResponseEntity with NOT_FOUND status
   */
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
    log.warn("Resource not found - URL: {}", request.getRequestURL());

    final ErrorApiResponse<Object> errorApiResponse = ApiResponse.error(HttpStatus.NOT_FOUND, request);

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorApiResponse);
  }

  /**
   * Handles HTTP message not readable exceptions.
   * <p> HttpMessageNotReadableException 是 Spring 框架中的一个重要异常，主要负责处理 HTTP 请求体解析失败的情况。
   * <p> 请求体缺失
   * <p> 请求体解析失败
   * <p> 数据类型转换错误
   *
   * @param ex      HttpMessageNotReadableException
   * @param request HttpServletRequest
   * @return ResponseEntity with BAD_REQUEST status
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex,
      HttpServletRequest request) {
    log.warn("Message parsing error - URL: {} - Error: {}",
        request.getRequestURL().toString(),
        ex.getMostSpecificCause().getMessage());

    final ErrorApiResponse<Object> errorApiResponse = ApiResponse.error(HttpStatus.BAD_REQUEST, request);
    errorApiResponse.setError(ErrorCode.MESSAGE_PARSE_ERROR.toErrorInfo());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorApiResponse);
  }

  /**
   * Handles missing servlet request parameter exceptions.
   *
   * @param ex      MissingServletRequestParameterException
   * @param request HttpServletRequest
   * @return ResponseEntity with BAD_REQUEST status
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<?> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex, HttpServletRequest request) {
    log.warn("Missing parameter - URL: {} - Parameter: {} - Type: {}",
        request.getRequestURL().toString(),
        ex.getParameterName(),
        ex.getParameterType());

    List<ErrorField> details = new ArrayList<>();
    ErrorField detail = new ErrorField();
    detail.setField(ex.getParameterName());

    final ErrorApiResponse<Object> errorApiResponse = ApiResponse.error(HttpStatus.BAD_REQUEST, request);
    errorApiResponse.setError(ErrorCode.PARAM_MISSING.toErrorInfo());
    errorApiResponse.setDetails(details);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorApiResponse);
  }

  /**
   * Handles method argument not valid exceptions.
   *
   * @param ex      MethodArgumentNotValidException
   * @param request HttpServletRequest
   * @return ResponseEntity with BAD_REQUEST status
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    log.warn("Validation failed - URL: {} - Errors: {}",
        request.getRequestURL().toString(),
        ex.getBindingResult().getFieldErrors());

    List<ErrorField> details = new ArrayList<>();
    ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
      String fieldToUse = getField(fieldError.getField());
      ErrorField detail = new ErrorField();
      detail.setField(fieldToUse);
      detail.setMessage(fieldError.getDefaultMessage());
      details.add(detail);
    });

    final ErrorApiResponse<Object> errorApiResponse = ApiResponse.error(HttpStatus.BAD_REQUEST, request);
    errorApiResponse.setError(ErrorCode.VALIDATION_FAILED.toErrorInfo());
    errorApiResponse.setDetails(details);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorApiResponse);
  }

  /**
   * Handles bind exceptions.
   *
   * @param ex      BindException
   * @param request HttpServletRequest
   * @return ResponseEntity with BAD_REQUEST status
   */
  @ExceptionHandler(BindException.class)
  public ResponseEntity<?> handleBindException(BindException ex, HttpServletRequest request) {
    log.warn("Bind exception occurred - URL: {}", request.getRequestURL());

    List<ErrorField> details = new ArrayList<>();
    ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
      String field = fieldError.getField();
      String defaultMessage = fieldError.getDefaultMessage();
      ErrorField detail = new ErrorField();
      detail.setField(field);
      detail.setMessage(defaultMessage);
      details.add(detail);
    });

    final ErrorApiResponse<Object> errorApiResponse = ApiResponse.error(HttpStatus.BAD_REQUEST, request);
    errorApiResponse.setError(ErrorCode.VALIDATION_FAILED.toErrorInfo());
    errorApiResponse.setDetails(details);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorApiResponse);
  }

  /**
   * Handles validation exceptions.
   *
   * @param ex      ValidationException
   * @param request HttpServletRequest
   * @return ResponseEntity with BAD_REQUEST status
   */
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<?> handleValidationException(ValidationException ex, HttpServletRequest request) {
    log.warn("Validation exception occurred - URL: {} - Error: {}",
        request.getRequestURL().toString(),
        ex.getMessage());

    // 构建字段错误详情列表
    List<ErrorField> details = new ArrayList<>();
    if (ex instanceof jakarta.validation.ConstraintViolationException constraintViolationException) {

      constraintViolationException.getConstraintViolations().forEach(violation -> {
        String field = violation.getPropertyPath().toString();
        String message = violation.getMessage();
        details.add(ErrorField.builder()
            .field(field)
            .message(message)
            .build());
      });
    }

    // 构建错误响应
    final ErrorApiResponse<List<ErrorField>> errorApiResponse = ApiResponse.error(HttpStatus.BAD_REQUEST, request);
    errorApiResponse.setError(ErrorCode.VALIDATION_FAILED.toErrorInfo());
    errorApiResponse.setData(details);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorApiResponse);
  }

  /**
   * Handles access denied exceptions.
   *
   * @param ex      AccessDeniedException
   * @param request HttpServletRequest
   * @return ResponseEntity with FORBIDDEN status
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
    log.warn("Access denied - URL: {}", request.getRequestURL());
    final ErrorApiResponse<Object> errorApiResponse = ApiResponse.error(HttpStatus.FORBIDDEN, request);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorApiResponse);
  }

  /**
   * Handles business exceptions.
   *
   * @param ex      BusinessException
   * @param request HttpServletRequest
   * @return ResponseEntity with appropriate status
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<?> handleBusinessException(BusinessException ex, HttpServletRequest request) {
    log.warn("Business error occurred - URL: {}", request.getRequestURL(), ex);
    final ErrorApiResponse<Object> errorApiResponse = ApiResponse.error(ex.getHttpStatus(), request);
    errorApiResponse.setError(ex.getErrorCode().toErrorInfo());
    return ResponseEntity.status(ex.getHttpStatus()).body(errorApiResponse);
  }

  /**
   * Handles system exceptions.
   *
   * @param ex      SystemException
   * @param request HttpServletRequest
   * @return ResponseEntity with appropriate status
   */
  @ExceptionHandler(SystemException.class)
  public ResponseEntity<?> handleSystemException(SystemException ex, HttpServletRequest request) {
    log.warn("System error occurred - URL: {}", request.getRequestURL(), ex);
    final ErrorApiResponse<Object> errorApiResponse = ApiResponse.error(ex.getHttpStatus(), request);
    errorApiResponse.setError(ex.getErrorCode().toErrorInfo());
    return ResponseEntity.status(ex.getHttpStatus()).body(errorApiResponse);
  }

  /**
   * Handles unexpected exceptions.
   *
   * @param ex      Exception
   * @param request HttpServletRequest
   * @return ResponseEntity with INTERNAL_SERVER_ERROR status
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleException(Exception ex, HttpServletRequest request) {
    log.error("Unexpected error occurred - URL: {}", request.getRequestURL(), ex);
    final ErrorApiResponse<Object> errorApiResponse = ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, request);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorApiResponse);
  }

  /**
   * Retrieves the field name from the message source.
   *
   * @param field Field key
   * @return Field name
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
