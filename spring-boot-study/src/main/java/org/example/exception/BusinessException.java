package org.example.exception;

import lombok.Getter;
import org.example.constant.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

  private final HttpStatus httpStatus;
  private final ErrorCode errorCode;

  public BusinessException(HttpStatus httpStatus, ErrorCode errorCode, String message) {
    super(message);
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
  }

}
