package org.example.constants;

import lombok.Getter;

/**
 * 统一响应码枚举
 */
@Getter
public enum ResponseCode {
  SUCCESS(200, "Success"),
  BAD_REQUEST(400, "Bad Request"),
  UNAUTHORIZED(401, "Authentication Required"),
  FORBIDDEN(403, "Authorization Required"),
  NOT_FOUND(404, "Not Found"),
  INTERNAL_ERROR(500, "Error");

  private final int code;
  private final String message;

  ResponseCode(int code, String message) {
    this.code = code;
    this.message = message;
  }
}