package org.example.constants;

/**
 * 服务端错误枚举
 */
public enum ServerErrorCode implements ErrorCode {
  DATABASE_ERROR(50000001, "数据库操作失败");

  private final int code;
  private final String message;

  ServerErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
