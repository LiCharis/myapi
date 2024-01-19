package com.my.springbootinit.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.myapicommon.model.InterfaceInfo;
import com.my.springbootinit.model.dto.intefaceInfo.InterfaceInfoQueryRequest;



/**
* @author Li
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-01-06 22:32:14
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 获取查询条件
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);
}
