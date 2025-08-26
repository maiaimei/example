package cn.maiaimei.enumeration;

import lombok.Getter;
import cn.maiaimei.model.response.error.ErrorInfo;

/**
 * 错误码枚举
 */
@Getter
public enum ErrorCode {
  // 客户端错误
  METHOD_NOT_ALLOWED("client_unsupported_request_method", "Unsupported request method"),
  MESSAGE_PARSE_ERROR("client_message_parsing_error", "Message parsing error"),
  PARAM_MISSING("client_missing_parameter", "Missing parameter"),
  PARAMETER_INVALID("client_invalid_parameter", "Invalid parameter"),
  VALIDATION_FAILED("client_validation_failed", "Validation failed");

  // 服务端错误

  // 数据库错误

  // 第三方错误

  // 业务错误

  // 系统错误

  private final String code;
  private final String message;

  ErrorCode(String code, String message) {
    this.code = code;
    this.message = message;
  }

  /**
   * 根据当前枚举返回对应的 ErrorInfo 对象
   *
   * @return ErrorInfo 对象
   */
  public ErrorInfo toErrorInfo() {
    return ErrorInfo.builder()
        .code(this.code)
        .message(this.message)
        .build();
  }
}
