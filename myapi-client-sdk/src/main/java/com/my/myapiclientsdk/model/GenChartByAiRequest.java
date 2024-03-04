package com.my.myapiclientsdk.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件上传请求
 */
@Data
public class GenChartByAiRequest implements Serializable {


    /**
     * 图表名字
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 生成策略
     */
    private String strategy;

    private static final long serialVersionUID = 1L;
}