package com.slice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.slice.dao.user.UserQueryRequest;
import com.slice.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.slice.vo.user.UserInfoVO;

/**
 * 用户服务
* @author zhangchuyuan
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param phone 手机号
     * @param password 密码
     * @return 新用户id
     */
    long userRegister(String phone, String password);

    /**
     * 用户登录
     *
     * @param phone  手机号
     * @param password 密码
     * @return 用户信息
     */
    UserInfoVO userLogin(String phone, String password);

    /**
     * 分页查询用户
     * @param userQueryRequest 参数体
     * @return 用户列表
     */
    Page<UserInfoVO> userQuery(UserQueryRequest userQueryRequest);



    /**
     * 获取脱敏的已登录用户信息
     * @param user 用户对象
     * @return 脱敏对象
     */
    UserInfoVO getUserInfoVO(User user);

    /**
     * 获取当前用户，根据token
     * @return
     */
    User getCurrentUser();





}
