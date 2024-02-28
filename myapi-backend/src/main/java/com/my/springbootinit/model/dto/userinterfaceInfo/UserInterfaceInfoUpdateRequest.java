package com.my.springbootinit.model.dto.userinterfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新请求
 *
 
 */
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;


    /**
     * 用户状态 0-正常 1-禁用
     */
    private Integer status;

    /**
     * 接口调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}