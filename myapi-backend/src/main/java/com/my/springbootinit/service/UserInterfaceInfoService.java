package com.my.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.myapicommon.model.UserInterfaceInfo;
import com.my.springbootinit.model.dto.userinterfaceInfo.UserInterfaceInfoQueryRequest;

import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Li
* @description 针对表【user_interface_info(用户-接口信息)】的数据库操作Service
* @createDate 2024-01-09 21:04:44
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 获取查询条件
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);


    /**
     * 查询调用记录
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    UserInterfaceInfo getUserInterfaceInfo(long userId, long interfaceInfoId);


    /**
     * 统计接口调用次数
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    boolean invokeCount(long userId,long interfaceInfoId);
}
