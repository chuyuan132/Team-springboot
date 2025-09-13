package com.slice.common;

import com.slice.enums.ErrorCode;
import lombok.Data;

/**
 * 通用返回类
 * @author zhangchuyuan
 */
@Data
public class BaseResponse<T> {
    private int code;

    private String message;

    private T data;

    /**
     * 成功响应构造函数
     */
    public BaseResponse(ErrorCode errorCode, T data) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = data;
    }

    /**
     * 错误响应构造函数
     */
    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    /**
     * 错误响应构造函数（使用错误码枚举）
     */
    public BaseResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = null;
    }
}
