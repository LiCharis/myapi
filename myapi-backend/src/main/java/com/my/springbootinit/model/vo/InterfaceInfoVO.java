package com.my.springbootinit.model.vo;

import com.my.myapicommon.model.InterfaceInfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoVO extends InterfaceInfo implements Serializable {
    /**
     * 接口调用次数
     */
    private Integer totalNum;

    /**
     * 接口剩余次数
     */
    private Integer leftNum;

    private static final long serialVersionUID = 1L;
}
