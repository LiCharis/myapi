package com.my.springbootinit.service;

import com.my.springbootinit.common.BaseResponse;
import com.my.springbootinit.common.ThirdLoginRequest;
import com.my.springbootinit.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

public interface Login3rdTarget {
    String loginByGitee(String state, String code, HttpServletRequest request);

    String loginByGithub(String state, String code, HttpServletRequest request);

    BaseResponse<LoginUserVO> autoRegister3rdAndLogin(ThirdLoginRequest thirdLoginRequest, HttpServletRequest httpServletRequest);
}
