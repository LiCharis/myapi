package com.my.springbootinit.controller;

import com.my.myapicommon.common.ErrorCode;
import com.my.springbootinit.common.BaseResponse;
import com.my.springbootinit.common.ResultUtils;
import com.my.springbootinit.common.ThirdLoginRequest;
import com.my.springbootinit.model.vo.LoginUserVO;
import com.my.springbootinit.service.impl.Login3rdAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/login3rd")
@Slf4j
public class LoginController {
    @Resource
    Login3rdAdapter login3rdAdapter;

    @GetMapping("/gitee/callback")
    public String loginByGitee(String code, String state, HttpServletRequest request) {
        return login3rdAdapter.loginByGitee(state, code, request);
    }


    @GetMapping("/github/callback")
    public String loginByGithub(String code, String state, HttpServletRequest request) {
        return login3rdAdapter.loginByGithub(state, code, request);
    }

    /**
     * 前端获取到第三方用户信息调用登录接口
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse thirdPartyLogin(@RequestBody ThirdLoginRequest thirdLoginRequest, HttpServletRequest httpServletRequest) {
        BaseResponse<LoginUserVO> response = null;
        try {
            response = login3rdAdapter.autoRegister3rdAndLogin(thirdLoginRequest, httpServletRequest);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"第三方登录失败");
        }
        return response;
    }
}
