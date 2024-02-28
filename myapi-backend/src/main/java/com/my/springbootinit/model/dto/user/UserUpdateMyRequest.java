package com.my.springbootinit.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户更新个人信息请求
 *
 
 */
@Data
public class UserUpdateMyRequest implements Serializable {


    /**
     * 用户名
     */
    private String userAccount;

    /**
     * 用户当前的密码
     */
    private String userCurrentPassword;

    /**
     * 用户新密码
     */
    private String userNewPassword;

    /**
     * 用户确认密码
     */
    private String userCheckPassword;



    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;



    private static final long serialVersionUID = 1L;
}