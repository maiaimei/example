package org.example.exception;

public class InvalidPasswordFormatException extends RuntimeException {

  public InvalidPasswordFormatException(String message) {
    super(message);
  }

  public InvalidPasswordFormatException(String message, Throwable cause) {
    super(message, cause);
  }
}