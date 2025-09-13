package com.slice.controller;

import com.slice.common.BaseResponse;
import com.slice.constant.UserConstant;
import com.slice.utils.ResultUtils;
import com.slice.dao.user.UserLoginRequest;
import com.slice.dao.user.UserRegisterRequest;
import com.slice.enums.ErrorCode;
import com.slice.exception.BusinessException;
import com.slice.service.UserService;
import com.slice.vo.user.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务控制层
 * @author zhangchuyuan
 */
@RestController
@RequestMapping("/user")
@Tag(name="用户接口")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String password = userRegisterRequest.getPassword();
        String phone = userRegisterRequest.getPhone();
        if(StringUtils.isAnyBlank(password, phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(phone, password);
        return ResultUtils.success(result);
    }


    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public BaseResponse<UserInfoVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if(userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String password = userLoginRequest.getPassword();
        String phone = userLoginRequest.getPhone();
        if(StringUtils.isAnyBlank(password, phone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInfoVO result = userService.userLogin(phone, password);
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, result.getId());
        return ResultUtils.success(result);
    }
}
