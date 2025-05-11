package org.example.constants;

import lombok.Getter;

/**
 * 错误码枚举 规则：
 * <p>
 * - 前三位: 错误类型(400-客户端错误, 500-服务端错误, 600-业务错误, 700-第三方服务错误)
 * </p>
 * <p>
 * - 中间两位: 具体模块(00-通用, 01-用户, 02-订单...)
 * </p>
 * <p>
 * - 后三位: 具体错误
 * </p>
 */
@Getter
public enum ErrorCode {
  // 客户端错误
  METHOD_NOT_ALLOWED(400020, "Unsupported request method"),
  MESSAGE_PARSE_ERROR(400001, "Message parsing error"),
  PARAM_MISSING(400001, "Missing parameter"),
  VALIDATION_FAILED(400701, "Validation failed"),
  PARAMETER_INVALID(40000006, "Invalid parameter");

  private final int code;
  private final String message;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
