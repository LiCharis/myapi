package com.my.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.myapicommon.model.UserInterfaceInfo;
import com.my.myapicommon.service.InnerUserInterfaceInfoService;
import com.my.myapicommon.common.ErrorCode;
import com.my.springbootinit.constant.CommonConstant;
import com.my.springbootinit.exception.BusinessException;
import com.my.springbootinit.exception.ThrowUtils;
import com.my.springbootinit.mapper.UserInterfaceInfoMapper;
import com.my.springbootinit.model.dto.userinterfaceInfo.UserInterfaceInfoQueryRequest;
import com.my.springbootinit.service.UserInterfaceInfoService;
import com.my.springbootinit.utils.SqlUtils;
import javassist.runtime.Inner;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author Li
 * @description 针对表【user_interface_info(用户-接口信息)】的数据库操作Service实现
 * @createDate 2024-01-09 21:04:44
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl
        implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long userId, long interfaceInfoId) {

        return userInterfaceInfoService.invokeCount(userId, interfaceInfoId);
    }

    @Override
    public UserInterfaceInfo getUserInterfaceInfo(long userId, long interfaceInfoId) {
        return userInterfaceInfoService.getUserInterfaceInfo(userId, interfaceInfoId);
    }

}




