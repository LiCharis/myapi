package com.my.springbootinit.model.dto.intefaceInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.my.springbootinit.annotation.RequestKeyParam;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用请求
 *
 
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {
    /**
     * 接口id
     */
    //加入自定义注解，表示该字段需要鉴定是否是重复请求
    @RequestKeyParam
    private Long id;

    /**
     * 用户id
     */
    @RequestKeyParam
    private Long userId;

    /**
     * 请求参数
     */
    @RequestKeyParam
    private String userRequestBody;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}