package com.my.myapicommon.service;


import com.my.myapicommon.model.User;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */

public interface InnerUserService {


   User getInvokeUser(String accessKey);

}
