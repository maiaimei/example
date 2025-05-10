package org.example.advice;

import jakarta.validation.ValidationException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.constants.ResponseCode;
import org.example.exception.BusinessException;
import org.example.model.response.ApiError;
import org.example.model.response.ApiErrorDetail;
import org.example.model.response.ApiErrorResponse;
import org.example.utils.ResponseUtils;
import org.springframework.context.MessageSource;
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
    
    private final MessageSource messageSource;

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse<?> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseUtils.error(ResponseCode.NOT_FOUND);
    }

    /**
     * HttpMessageNotReadableException 是 Spring 框架中的一个重要异常，主要负责处理 HTTP 请求体解析失败的情况。
     * 请求体缺失
     * 请求体解析失败
     * 数据类型转换错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse<?> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException ex) {
        return ResponseUtils.error(ResponseCode.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse<?> handleMissingServletRequestParameterException(
        MissingServletRequestParameterException ex) {
        final ApiError error = new ApiError();
        error.setTypeKey("");
        error.setMessageKey("");

        List<ApiErrorDetail> details = new ArrayList<>();
        ApiErrorDetail detail = new ApiErrorDetail();
        detail.setFieldKey(ex.getParameterName());
        detail.setMessageKey("Parameter is missing");

        return ResponseUtils.error(ResponseCode.VALIDATION_ERROR, error, details);
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        final ApiError error = new ApiError();
        error.setTypeKey("");
        error.setMessageKey("");

        List<ApiErrorDetail> details = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            ApiErrorDetail detail = new ApiErrorDetail();
            detail.setFieldKey(field);
            detail.setMessageKey(defaultMessage);
            details.add(detail);
        });

        return ResponseUtils.error(ResponseCode.VALIDATION_ERROR, error, details);
    }
    
    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse<?> handleBindException(BindException ex) {
        final ApiError error = new ApiError();
        error.setTypeKey("");
        error.setMessageKey("");

        List<ApiErrorDetail> details = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            ApiErrorDetail detail = new ApiErrorDetail();
            detail.setFieldKey(field);
            detail.setMessageKey(defaultMessage);
            details.add(detail);
        });
        
        return ResponseUtils.error(ResponseCode.VALIDATION_ERROR, error, details);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse<?> handleValidationException(ValidationException ex) {
        final ApiError error = new ApiError();
        error.setTypeKey("");
        error.setMessageKey("");

        return ResponseUtils.error(ResponseCode.VALIDATION_ERROR, error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorResponse<?> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseUtils.error(ResponseCode.FORBIDDEN);
    }
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse<?> handleBusinessException(BusinessException ex) {
        return ResponseUtils.error(ex.getResponseCode());
    }
    
    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse<?> handleException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return ResponseUtils.error(ResponseCode.INTERNAL_ERROR);
    }
}
