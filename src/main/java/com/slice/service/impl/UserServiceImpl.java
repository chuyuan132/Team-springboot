package com.slice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slice.constant.UserConstant;
import com.slice.dao.user.UserQueryRequest;
import com.slice.dao.user.UserUpdateRequest;
import com.slice.entity.User;
import com.slice.enums.ErrorCode;
import com.slice.exception.BusinessException;
import com.slice.service.UserService;
import com.slice.mapper.UserMapper;
import com.slice.utils.DateFormatUtils;
import com.slice.vo.user.UserInfoVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户服务实现
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    /**
     * 盐值
     */
    public static final String SALT = "SLICE_ZCY";


    @Resource
    private UserMapper userMapper;


    @Resource
    private DateFormatUtils dateFormatUtils;


    @Override
    public long userRegister(String phone, String password) {
        synchronized (phone.intern()) {
            Matcher matcher = Pattern.compile("^1[3-9]\\d{9}$").matcher(phone);

            if(!matcher.matches()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号格式错误");
            }

            if(password.length() < 8) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能少于8位");
            }

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", phone);
            Long count = userMapper.selectCount(queryWrapper);
            if(count > 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户已存在");
            }
            String newPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
            User user = new User();
            user.setPhone(phone);
            user.setPassword(newPassword);
            int saveResult = userMapper.insert(user);
            if(saveResult <= 0) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户注册失败");
            }
            return user.getId();
        }
    }

    @Override
    public UserInfoVO userLogin(String phone, String password) {
        Matcher matcher = Pattern.compile("^1[3-9]\\d{9}$").matcher(phone);

        if(!matcher.matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号格式错误");
        }

        if(password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能少于8位");
        }
        String encrpytPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", phone);
        userQueryWrapper.eq("password", encrpytPassword);
        User user = userMapper.selectOne(userQueryWrapper);
        if(user == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户不存在或密码错误");
        }
        return getUserInfoVO(user);
    }

    @Override
    public Page<UserInfoVO> userQuery(UserQueryRequest userQueryRequest) {
        String phone = userQueryRequest.getPhone();
        String username = userQueryRequest.getUsername();
        int pageNo = userQueryRequest.getPageNo();
        int pageSize = userQueryRequest.getPageSize();
        List<String> tagList = userQueryRequest.getTagList();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(phone)) {
            userQueryWrapper.eq("phone", phone);
        }
        if(StringUtils.isNotBlank(username)) {
            userQueryWrapper.like("username", username);
        }
        if(!tagList.isEmpty()) {
            tagList.stream().filter(StringUtils::isNotBlank).forEach(val -> {
                userQueryWrapper.like("tag_list", val);
            });
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNo, pageSize), userQueryWrapper);
        // 转换对象
        List<UserInfoVO> userInfoVOS = userPage.getRecords().stream()
                .map(this::getUserInfoVO)
                .toList();
        // 重新封装返回结果
        Page<UserInfoVO> userInfoVOPage = new Page<>(userPage.getCurrent(), userPage.getSize());
        userInfoVOPage.setRecords(userInfoVOS);
        userInfoVOPage.setTotal(userPage.getTotal());
        return userInfoVOPage;
    }

    @Override
    public void userUpdate(UserUpdateRequest userUpdateRequest) {
        User user = new User();
        user.setPassword(userUpdateRequest.getPassword());
        user.setUsername(userUpdateRequest.getUsername());
        user.setAvatar(userUpdateRequest.getAvatar());
        user.setEmail(userUpdateRequest.getEmail());
        if(!userUpdateRequest.getTagList().isEmpty()) {
            Gson gson = new Gson();
            user.setTagList(gson.toJson(userUpdateRequest.getTagList()));
        }
        int i = userMapper.updateById(user);
        if(i <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public void userDelete(long id) {
        int i = userMapper.deleteById(id);
        if(i <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public UserInfoVO getUserInfoVO(User user) {
        if (user == null) {
            return null;
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        if(!StringUtils.isBlank(user.getTagList())) {
            Gson gson = new Gson();
            List<String> tagList = gson.fromJson(user.getTagList(), new TypeToken<List<String>>(){}).stream().filter(StringUtils::isBlank).toList();
            userInfoVO.setTagList(tagList);
        } else {
            userInfoVO.setTagList(Collections.emptyList());
        }
        userInfoVO.setCreatedAt(dateFormatUtils.formatDate(user.getCreatedAt()));
        userInfoVO.setUpdatedAt(dateFormatUtils.formatDate(user.getUpdatedAt()));
        return userInfoVO;
    }

    @Override
    public User getCurrentUser() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        long attribute = (long)request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        return userMapper.selectById(attribute);

    }
}




