package org.example.exception;

public class PasswordEncoderException extends RuntimeException {

  public PasswordEncoderException(String message) {
    super(message);
  }

  public PasswordEncoderException(String message, Throwable cause) {
    super(message, cause);
  }
}