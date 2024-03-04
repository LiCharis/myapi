package com.my.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.myapicommon.model.InterfaceInfo;
import com.my.myapicommon.model.UserInterfaceInfo;
import com.my.myapicommon.common.ErrorCode;
import com.my.springbootinit.constant.CommonConstant;
import com.my.springbootinit.exception.BusinessException;
import com.my.springbootinit.exception.ThrowUtils;
import com.my.springbootinit.model.dto.userinterfaceInfo.UserInterfaceInfoQueryRequest;
import com.my.springbootinit.service.InterfaceInfoService;
import com.my.springbootinit.service.UserInterfaceInfoService;
import com.my.springbootinit.mapper.UserInterfaceInfoMapper;
import com.my.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * @author Li
 * @description 针对表【user_interface_info(用户-接口信息)】的数据库操作Service实现
 * @createDate 2024-01-09 21:04:44
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfo.getTotalNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();


        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(userId <= 0 || interfaceInfoId <= 0, ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (totalNum > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口调用次数不能超出限额(50)");
        }
        if (leftNum < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口剩余调用次数不能小于0");
        }
    }


    /**
     * 获取查询包装类
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (userInterfaceInfoQueryRequest == null) {
            return queryWrapper;
        }
        Long id = userInterfaceInfoQueryRequest.getId();
        Long userId = userInterfaceInfoQueryRequest.getUserId();
        Long interfaceInfoId = userInterfaceInfoQueryRequest.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfoQueryRequest.getTotalNum();
        Integer leftNum = userInterfaceInfoQueryRequest.getLeftNum();
        Integer status = userInterfaceInfoQueryRequest.getStatus();
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long pageSize = userInterfaceInfoQueryRequest.getPageSize();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 查询记录是否存在/判断剩余次数是否已用尽/不存在则新创记录
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    @Override
    public UserInterfaceInfo getUserInterfaceInfo(long userId, long interfaceInfoId) {
        QueryWrapper<UserInterfaceInfo> userInterfaceInfoQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId",interfaceInfoId);
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        String name = interfaceInfo.getName();
        userInterfaceInfoQueryWrapper.eq("userId", userId);
        userInterfaceInfoQueryWrapper.eq("interfaceInfoId", interfaceInfoId);
        UserInterfaceInfo userInterfaceInfo = this.getOne(userInterfaceInfoQueryWrapper);

        if (userInterfaceInfo == null) {
            userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setUserId(userId);
            userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
            userInterfaceInfo.setTotalNum(0);
            userInterfaceInfo.setLeftNum(20);
            userInterfaceInfo.setName(name);
            boolean save = this.save(userInterfaceInfo);
            if (!save) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入调用记录失败");
            }
        }
        return userInterfaceInfo;
    }


    /**
     * 事务方法，在操作mysql时添加行锁
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    @Transactional
    @Override
    public boolean invokeCount(long userId, long interfaceInfoId) {
        //判断id是否有效
        if (userId <= 0 || interfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        //调用接口后的该用户调用次数变化
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userId", userId);
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        //leftNum不能小于0
        updateWrapper.gt("leftNum", 0);

        //调用后接口总次数变化
        UpdateWrapper<InterfaceInfo> interfaceInfoUpdateWrapper = new UpdateWrapper<>();
        interfaceInfoUpdateWrapper.eq("id",interfaceInfoId);
        interfaceInfoUpdateWrapper.gt("leftNum",0);
        /**
         * 这里加了行锁，一条一条执行@Transactional
         */
        updateWrapper.setSql("totalNum = totalNum + 1,leftNum = leftNum - 1");
        interfaceInfoUpdateWrapper.setSql("totalNum = totalNum + 1,leftNum = leftNum - 1");
        return this.update(updateWrapper) && interfaceInfoService.update(interfaceInfoUpdateWrapper);

    }
}




