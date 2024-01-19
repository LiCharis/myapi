package com.my.springbootinit.common;

import com.google.gson.Gson;
import lombok.Data;

@Data
public class ContentBody<T> {

    /**
     * 参数数据
     */
    private T data;

}
