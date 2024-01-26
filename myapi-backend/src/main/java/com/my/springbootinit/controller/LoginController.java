package com.my.springbootinit.controller;

import com.my.springbootinit.common.BaseResponse;
import com.my.springbootinit.service.impl.Login3rdAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/login3rd")
@Slf4j
public class LoginController {
    @Resource
    Login3rdAdapter login3rdAdapter;
    @GetMapping("/gitee/callback")
    public String loginByGitee(String code, String state, HttpServletRequest request){
        return login3rdAdapter.loginByGitee(state,code,request);
    }

    @GetMapping("/github/callback")
    public String loginByGithub(String code,String state,HttpServletRequest request){
        return login3rdAdapter.loginByGithub(state,code,request);
    }
}
