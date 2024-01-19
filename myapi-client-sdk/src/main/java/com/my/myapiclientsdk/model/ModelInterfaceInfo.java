package com.my.myapiclientsdk.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 接口信息
 * @TableName model_interface_info
 */
@TableName(value ="model_interface_info")
@Data
public class ModelInterfaceInfo implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 服务器地址
     */
    private String host;

    /**
     * 服务器端口
     */
    private String port;

    /**
     * 
     */
    private String account;

    /**
     * 服务器密码
     */
    private String password;

    /**
     * 参数类型
     */
    private String img_order;

    /**
     * 模型环境
     */
    private String env;

    /**
     * 模型路径
     */
    private String model_path;

    /**
     * 执行命令
     */
    private String invoke_order;

    /**
     * 结果所存放的本地地址
     */
    private String local_path;

    /**
     * 结果生成地址
     */
    private String result_path;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 
     */
    private Long interfaceInfoId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}