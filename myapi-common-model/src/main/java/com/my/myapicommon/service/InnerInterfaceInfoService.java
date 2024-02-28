package com.my.myapicommon.service;

import com.my.myapicommon.model.InterfaceInfo;

/**
 * @author Li
 * @description 针对表【interface_info(接口信息)】的数据库操作Service
 * @createDate 2024-01-06 22:32:14
 */
public interface InnerInterfaceInfoService {
    InterfaceInfo getInterfaceInfo(Long id);

}
