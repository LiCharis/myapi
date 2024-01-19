package com.my.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.my.myapiclientsdk.model.ModelInterfaceInfo;
import com.my.springbootinit.service.ModelInterfaceInfoService;
import com.my.springbootinit.mapper.ModelInterfaceInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author Li
* @description 针对表【model_interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-01-14 17:13:49
*/
@Service
public class ModelInterfaceInfoServiceImpl extends ServiceImpl<ModelInterfaceInfoMapper, ModelInterfaceInfo>
    implements ModelInterfaceInfoService{

}




