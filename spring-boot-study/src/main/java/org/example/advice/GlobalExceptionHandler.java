package org.example.advice;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.constants.ClientErrorCode;
import org.example.constants.ResponseCode;
import org.example.exception.BusinessException;
import org.example.model.response.ApiResponse;
import org.example.model.response.ApiResponse.BasicResponse;
import org.example.model.response.ApiResponse.ErrorInfo;
import org.example.model.response.ApiResponse.FieldError;
import org.springframework.boot.json.JsonParseException;
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

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public BasicResponse handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
    return ApiResponse.error(ResponseCode.NOT_FOUND, request.getPathInfo(), request.getMethod());
  }

  /**
   * Handle HTTP message not readable exception
   * <p>
   * HttpMessageNotReadableException 是 Spring 框架中的一个重要异常，主要负责处理 HTTP 请求体解析失败的情况。 请求体缺失 请求体解析失败 数据类型转换错误
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BasicResponse handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex,
      HttpServletRequest request) {

    String requestUrl = request.getRequestURL().toString();
    Throwable cause = ex.getMostSpecificCause();

    ClientErrorCode errorCode = switch (cause) {
      case JsonParseException jsonParseException -> {
        log.warn("JSON parse error - URL: {} - Details: {}",
            requestUrl, cause.getMessage());
        yield ClientErrorCode.HTTP_MESSAGE_JSON_ERROR;
      }
      case InvalidFormatException invalidFormatEx -> {
        if (invalidFormatEx.getTargetType() != null &&
            invalidFormatEx.getTargetType().isEnum()) {
          String invalidValue = String.valueOf(invalidFormatEx.getValue());
          log.warn("Invalid enum value [{}] for type [{}] - URL: {}",
              invalidValue,
              invalidFormatEx.getTargetType().getSimpleName(),
              requestUrl);
          yield ClientErrorCode.HTTP_MESSAGE_ENUM_ERROR;
        } else {
          log.warn("Type conversion error - Expected: [{}], Got: [{}] - URL: {}",
              invalidFormatEx.getTargetType().getSimpleName(),
              invalidFormatEx.getValue().getClass().getSimpleName(),
              requestUrl);
          yield ClientErrorCode.HTTP_MESSAGE_TYPE_ERROR;
        }
      }
      case MismatchedInputException mismatchedInputException -> {
        log.warn("Missing required field - URL: {} - Details: {}",
            requestUrl, cause.getMessage());
        yield ClientErrorCode.HTTP_MESSAGE_MISSING_FIELD;
      }
      case JsonMappingException jsonMappingException -> {
        log.warn("JSON mapping error - URL: {} - Details: {}",
            requestUrl, cause.getMessage());
        yield ClientErrorCode.HTTP_MESSAGE_JSON_MAPPING_ERROR;
      }
      default -> {
        log.error("Message parsing error - URL: {} - Error: {}",
            requestUrl, cause.getMessage());
        yield ClientErrorCode.HTTP_MESSAGE_NOT_READABLE;
      }
    };

    ErrorInfo errorInfo = ErrorInfo.builder()
        .code(errorCode.getCode())
        .message(errorCode.getMessage()).build();
    return ApiResponse.error(ResponseCode.BAD_REQUEST, request.getPathInfo(), request.getMethod(), errorInfo);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BasicResponse handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex, HttpServletRequest request) {
    log.warn("Missing parameter - URL: {} - Parameter: {} - Type: {}",
        request.getRequestURL(),
        ex.getParameterName(),
        ex.getParameterType());

    List<FieldError> details = new ArrayList<>();
    FieldError detail = new FieldError();
    detail.setField(ex.getParameterName());
    detail.setMessage("Parameter is missing");

    return ApiResponse.error(ResponseCode.BAD_REQUEST, request.getPathInfo(), request.getMethod(), details);
  }

  /**
   * 处理参数验证异常
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BasicResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    String requestUrl = request.getRequestURL().toString();
    List<String> logMessages = new ArrayList<>();
    List<FieldError> details = new ArrayList<>();
    ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
      String field = fieldError.getField();
      String fieldToUse = getField(field);
      String defaultMessage = fieldError.getDefaultMessage();
      logMessages.add(String.format("Field [%s]: %s", field, defaultMessage));
      FieldError detail = new FieldError();
      detail.setField(fieldToUse);
      detail.setMessage(fieldToUse + " " + defaultMessage);
      details.add(detail);
    });
    log.warn("Validation failed - URL: {} - Errors: {}",
        requestUrl,
        String.join("; ", logMessages));
    final ErrorInfo errorInfo = ErrorInfo.builder()
        .code(ClientErrorCode.REQUEST_PARAMETER_ERROR.getCode())
        .message(ClientErrorCode.REQUEST_PARAMETER_ERROR.getMessage()).build();
    return ApiResponse.error(ResponseCode.BAD_REQUEST, request.getPathInfo(), request.getMethod(), errorInfo, details);
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
   * @param fieldKey 字段键
   * @return 字段名称
   */
  private String getField(String fieldKey) {
    String fieldToUse = fieldKey;
    try {
      fieldToUse = fieldMessageSource.getMessage(fieldKey, null, null);
    } catch (NoSuchMessageException e) {
      log.error("Field message not found for key: {}", fieldKey);
    }
    return fieldToUse;
  }
}
