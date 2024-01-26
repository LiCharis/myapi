package com.my.springbootinit.service;

import com.my.springbootinit.common.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public interface Login3rdTarget {
    String loginByGitee(String state, String code, HttpServletRequest request);

    String loginByGithub(String state, String code, HttpServletRequest request);
}
