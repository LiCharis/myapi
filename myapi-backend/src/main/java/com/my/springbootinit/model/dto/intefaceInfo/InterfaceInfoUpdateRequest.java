package com.my.springbootinit.model.dto.intefaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 更新请求
 *
 
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 通讯协议
     */
    private String protocol;

    /**
     *主机地址
     */
    private String host;

    /**
     * 接口地址
     */
    private String path;

    /**
     * 参数类型
     */
    private String parameterType;

    /**
     * 请求参数
     */
    private String requestBody;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态 0-关闭 1-开启
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 接口调用次数
     */
    private Integer totalNum;

    /**
     * 接口剩余次数
     */
    private Integer leftNum;

    /**
     * 是否需要上传文件(0-不需要上传 1-需要上传)
     */
    private Integer isUpload;

    /**
     * 是否结果是否可以下载(0-不能 1-可以)
     */
    private Integer isDownload;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}