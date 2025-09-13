package com.slice.aop;

import com.slice.annotation.AuthCheck;
import com.slice.entity.User;
import com.slice.enums.ErrorCode;
import com.slice.enums.UserRoleEnums;
import com.slice.exception.BusinessException;
import com.slice.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;


    @Around("@annotation(authCheck)")
    public Object aroundInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        User currentUser = userService.getCurrentUser();
        // 不需要权限，放行
        if(mustRole == null) {
            return joinPoint.proceed();
        }
        // 注解权限对应的枚举
        UserRoleEnums mustRoleEnum = UserRoleEnums.getEnumByValue(mustRole);
        // 用户身份的枚举
        UserRoleEnums userRoleEnums = UserRoleEnums.getEnumByValue(currentUser.getUserRole());
        // 未知权限 - 抛错误
        if(mustRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        if(UserRoleEnums.BAN.equals(mustRoleEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        if(UserRoleEnums.ADMIN.equals(mustRoleEnum)) {
            if(!UserRoleEnums.ADMIN.equals(userRoleEnums)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }

        return joinPoint.proceed();
    }
}
