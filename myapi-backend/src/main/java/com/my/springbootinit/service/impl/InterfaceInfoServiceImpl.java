package com.my.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.myapicommon.model.InterfaceInfo;
import com.my.springbootinit.common.ErrorCode;
import com.my.springbootinit.constant.CommonConstant;
import com.my.springbootinit.exception.BusinessException;
import com.my.springbootinit.exception.ThrowUtils;
import com.my.springbootinit.model.dto.intefaceInfo.InterfaceInfoQueryRequest;
import com.my.springbootinit.model.dto.post.PostQueryRequest;


import com.my.springbootinit.model.entity.Post;
import com.my.springbootinit.service.InterfaceInfoService;
import com.my.springbootinit.mapper.InterfaceInfoMapper;
import com.my.springbootinit.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Li
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2024-01-06 22:32:14
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String protocol = interfaceInfo.getProtocol();
        String host = interfaceInfo.getHost();
        String path = interfaceInfo.getPath();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();


        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, description, protocol, host, path, requestHeader, responseHeader), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长");
        }
        if (StringUtils.isNotBlank(description) && description.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口描述过长");
        }
    }


    /**
     * 获取查询包装类
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceInfoQueryRequest == null) {
            return queryWrapper;
        }
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String protocol = interfaceInfoQueryRequest.getProtocol();
        String host = interfaceInfoQueryRequest.getHost();
        String path = interfaceInfoQueryRequest.getPath();
        String parameterType = interfaceInfoQueryRequest.getParameterType();
        String requestBody = interfaceInfoQueryRequest.getRequestBody();
        String requestHeader = interfaceInfoQueryRequest.getRequestHeader();
        String responseHeader = interfaceInfoQueryRequest.getResponseHeader();
        Integer status = interfaceInfoQueryRequest.getStatus();
        String method = interfaceInfoQueryRequest.getMethod();
        Long userId = interfaceInfoQueryRequest.getUserId();
        long current = interfaceInfoQueryRequest.getCurrent();
        long pageSize = interfaceInfoQueryRequest.getPageSize();



        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);

        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(description),"description",description);
        queryWrapper.eq(ObjectUtils.isNotEmpty(protocol), "protocol", protocol);
        queryWrapper.eq(ObjectUtils.isNotEmpty(host), "host", host);
        queryWrapper.eq(ObjectUtils.isNotEmpty(path), "path", path);
        queryWrapper.eq(ObjectUtils.isNotEmpty(parameterType), "parameterType", parameterType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(requestBody), "requestBody", requestBody);
        queryWrapper.eq(ObjectUtils.isNotEmpty(requestHeader), "requestHeader", requestHeader);
        queryWrapper.eq(ObjectUtils.isNotEmpty(responseHeader), "responseHeader", responseHeader);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(method), "method", method);


        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




