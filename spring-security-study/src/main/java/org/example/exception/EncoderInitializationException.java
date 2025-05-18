package org.example.exception;

public class EncoderInitializationException extends RuntimeException {

  public EncoderInitializationException(String message) {
    super(message);
  }

  public EncoderInitializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
