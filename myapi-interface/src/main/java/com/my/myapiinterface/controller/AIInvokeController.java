package com.my.myapiinterface.controller;


import com.my.myapiinterface.api.AiManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/invoke")
public class AIInvokeController {

    @Resource
    private AiManager aiManager;

    @RequestMapping("/yucongming")
    public String invokeYuCongMingAI(@RequestParam long modelId,@RequestParam String message){

        String response = aiManager.doChat(modelId, message);
        return response;

    }
}
