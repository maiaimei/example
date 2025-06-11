package org.example.mybatis.exception;

public class InvalidSqlException extends RuntimeException {

  public InvalidSqlException(String message) {
    super(message);
  }
}
