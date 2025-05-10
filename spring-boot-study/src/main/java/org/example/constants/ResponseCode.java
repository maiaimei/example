package org.example.constants;

import lombok.Getter;

/**
 * 统一响应码枚举
 */
@Getter
public enum ResponseCode {
    // 成功响应码 (2xxxx)
    SUCCESS(20000, "response.success"),

    // 客户端错误码 (4xxxx)
    BAD_REQUEST(40000, "response.bad.request"),
    UNAUTHORIZED(40001, "response.unauthorized"),
    FORBIDDEN(40003, "response.forbidden"),
    NOT_FOUND(40004, "response.not.found"),

    // 数据验证错误码 (41000-41999)
    VALIDATION_ERROR(41000, "response.validation.error"),

    // 业务错误码 (42000-42999)
    BUSINESS_ERROR(42000, "response.business.error"),

    // 系统错误码 (5xxxx)
    INTERNAL_ERROR(50000, "response.internal.error");

    private final int code;
    private final String messageKey;

    ResponseCode(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }
}