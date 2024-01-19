package com.my.springbootinit.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.myapicommon.model.InterfaceInfo;
import com.my.myapicommon.model.UserInterfaceInfo;


import java.util.List;

/**
* @author Li
* @description 针对表【user_interface_info(用户-接口信息)】的数据库操作Mapper
* @createDate 2024-01-09 21:04:44
* @Entity com.my.springbootinit.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




