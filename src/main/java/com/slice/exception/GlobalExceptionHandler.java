package com.slice.exception;

import com.slice.common.BaseResponse;
import com.slice.enums.ErrorCode;
import com.slice.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author zhangchuyuan
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException: {}", e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }


    /**
     * 处理参数缺失或无请求体异常
     */
    @ExceptionHandler(value={HttpMessageNotReadableException.class,MissingServletRequestParameterException.class})
    public BaseResponse<?> paramsExceptionHandler(Exception e) {
        log.error("MissingServletRequestParameterException: {}", e.getMessage(), e);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR);
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException: {}", e.getMessage(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }




    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse<?> exceptionHandler(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }
}