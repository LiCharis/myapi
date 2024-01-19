package com.my.springbootinit.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.myapicommon.model.InterfaceInfo;
import com.my.myapicommon.model.UserInterfaceInfo;
import com.my.springbootinit.annotation.AuthCheck;
import com.my.springbootinit.common.BaseResponse;
import com.my.springbootinit.common.ErrorCode;
import com.my.springbootinit.common.ResultUtils;
import com.my.springbootinit.exception.BusinessException;
import com.my.springbootinit.mapper.InterfaceInfoMapper;
import com.my.springbootinit.mapper.UserInterfaceInfoMapper;
import com.my.springbootinit.model.vo.InterfaceInfoVO;
import com.my.springbootinit.service.InterfaceInfoService;
import com.my.springbootinit.service.UserInterfaceInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Component
@RequestMapping("/analyse")
public class InterfaceInfoAnalyseController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interfaceInfo/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        List<UserInterfaceInfo> interfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(5);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoMap =
                interfaceInfoList.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if (ObjectUtils.isEmpty(list)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceInfoVO> interfaceInfoVOList = list.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            int totalNum = interfaceInfoMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;

        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVOList);


    }
}
