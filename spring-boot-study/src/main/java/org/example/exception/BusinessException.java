package org.example.exception;

import lombok.Getter;
import org.example.constants.ResponseCode;

@Getter
public class BusinessException extends RuntimeException {

  private final ResponseCode responseCode;

  public BusinessException(ResponseCode responseCode, String message) {
    super(message);
    this.responseCode = responseCode;
  }

}
