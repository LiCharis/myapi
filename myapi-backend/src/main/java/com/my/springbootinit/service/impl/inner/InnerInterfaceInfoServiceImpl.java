package com.my.springbootinit.service.impl.inner;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.myapicommon.model.InterfaceInfo;
import com.my.myapicommon.service.InnerInterfaceInfoService;
import com.my.springbootinit.common.ErrorCode;

import com.my.springbootinit.exception.BusinessException;

import com.my.springbootinit.mapper.InterfaceInfoMapper;


import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;


import javax.annotation.Resource;

/**
 * @author Li
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2024-01-06 22:32:14
 */
@DubboService
public class InnerInterfaceInfoServiceImpl
        implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;


    @Override
    public InterfaceInfo getInterfaceInfo(Long id) {
       if (id == null){
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
       }
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
        interfaceInfoQueryWrapper.eq("id",id);
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(interfaceInfoQueryWrapper);
        if (interfaceInfo == null || interfaceInfo.getStatus() == 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在或者已被下线");
        }
        return interfaceInfo;
    }

}




