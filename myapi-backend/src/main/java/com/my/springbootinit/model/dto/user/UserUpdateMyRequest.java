package com.my.springbootinit.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户更新个人信息请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
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