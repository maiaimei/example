package org.example.exception;

import lombok.Getter;
import org.example.constant.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public class SystemException extends RuntimeException {

  private final HttpStatus httpStatus;
  private final ErrorCode errorCode;

  public SystemException(HttpStatus httpStatus, ErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
  }
}
