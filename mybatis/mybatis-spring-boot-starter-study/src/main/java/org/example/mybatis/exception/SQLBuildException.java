package org.example.mybatis.exception;

public class SQLBuildException extends RuntimeException {

  public SQLBuildException(String message) {
    super(message);
  }

  public SQLBuildException(String message, Throwable cause) {
    super(message, cause);
  }
}
