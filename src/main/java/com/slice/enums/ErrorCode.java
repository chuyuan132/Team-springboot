package com.slice.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS(200, "成功"),
    SYSTEM_ERROR(500, "系统异常，请稍后重试"),
    NO_AUTH_ERROR(401,"无权限"),
    PARAMS_ERROR(400,"参数错误"),
    NO_SUPPORT_ERROR(405,"不支持");

    private final int code;

    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }




}
