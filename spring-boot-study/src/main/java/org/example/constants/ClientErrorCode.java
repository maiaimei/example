package org.example.constants;

import lombok.Getter;

/**
 * 客户端错误码枚举
 */
@Getter
public enum ClientErrorCode implements ErrorCode {
  METHOD_ERROR(40000000, "Invalid request method"),
  HTTP_MESSAGE_NOT_READABLE(40000001, "Invalid request format"),
  HTTP_MESSAGE_JSON_ERROR(40000002, "Invalid json format"), // 错误的JSON格式
  HTTP_MESSAGE_JSON_MAPPING_ERROR(40000005, "Invalid json mapping"), // JSON映射错误
  HTTP_MESSAGE_TYPE_ERROR(40000002, "Data type mismatch"), // 数据类型转换错误
  HTTP_MESSAGE_ENUM_ERROR(40000003, "Invalid option value"),
  HTTP_MESSAGE_MISSING_FIELD(40000004, "Missing necessary fields"), // 输入不匹配错误
  REQUEST_PARAMETER_ERROR(40000006, "Request parameter error");

  private final int code;
  private final String message;

  ClientErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
