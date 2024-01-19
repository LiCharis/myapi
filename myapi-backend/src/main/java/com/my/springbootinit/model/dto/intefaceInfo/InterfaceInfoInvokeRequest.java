package com.my.springbootinit.model.dto.intefaceInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 请求参数
     */
    private String userRequestBody;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}