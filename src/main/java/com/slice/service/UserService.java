package com.slice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.slice.constant.UserConstant;
import com.slice.dao.user.UserQueryRequest;
import com.slice.dao.user.UserUpdateRequest;
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
     * 用户更新
     * @param userUpdateRequest 参数
     * @return
     */
    void userUpdate(UserUpdateRequest userUpdateRequest);


    /**
     * 用户删除
     * @param id 用户id
     * @return
     */
    void userDelete(long id);

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

    /**
     * 是否为管理员
     * @param user
     * @return
     */
    default boolean isAdmin(User user) {
        return user.getUserRole().equals(UserConstant.USER_ADMIN_ROLE);
    }



}
