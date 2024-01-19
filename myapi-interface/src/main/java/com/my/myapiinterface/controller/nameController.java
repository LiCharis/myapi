package com.my.myapiinterface.controller;

import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.my.myapiclientsdk.model.User;
import com.my.myapiclientsdk.signUtils.SignUtils;
import com.my.myapicommon.common.BaseResponse;
import com.my.myapicommon.common.ResultUtils;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/name")
public class nameController {



    @GetMapping("/get")
    public BaseResponse<String> getNameByGet(@RequestParam String name) {
        String result = "GET 你的名字是 " + name;
        return ResultUtils.success(result);
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是 " + name;
    }

    @PostMapping("/user")
    public BaseResponse<String> getUserNameByPost(@RequestBody User user, HttpServletRequest httpServletRequest) {


        String result = "POST 你的名字是 " + user.getUsername();
        return ResultUtils.success(result);

    }

}
