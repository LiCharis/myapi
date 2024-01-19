package com.my.myapicommon.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.myapicommon.model.UserInterfaceInfo;

/**
 * @author Li
 * @description 针对表【user_interface_info(用户-接口信息)】的数据库操作Service
 * @createDate 2024-01-09 21:04:44
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 统计接口调用次数
     *
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    boolean invokeCount(long userId, long interfaceInfoId);

    /**
     * 查询调用记录
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    UserInterfaceInfo getUserInterfaceInfo(long userId, long interfaceInfoId);
}
