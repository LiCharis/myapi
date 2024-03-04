package com.my.springbootinit.service.impl;


import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.myapicommon.model.User;
import com.my.springbootinit.common.BaseResponse;
import com.my.myapicommon.common.ErrorCode;
import com.my.springbootinit.exception.BusinessException;
import com.my.springbootinit.exception.ThrowUtils;
import com.my.springbootinit.model.vo.LoginUserVO;
import com.my.springbootinit.service.Login3rdTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.my.springbootinit.constant.UserConstant.USER_LOGIN_STATE;

@Slf4j
@Component
public class Login3rdAdapter extends UserServiceImpl implements Login3rdTarget {

    @Value("${gitee.state}")
    private String giteeState;
    @Value("${gitee.token.url}")
    private String giteeTokenUrl;
    @Value("${gitee.user.url}")
    private String giteeUserUrl;


    @Value("${github.state}")
    private String githubState;
    @Value("${github.token.url}")
    private String githubTokenUrl;
    @Value("${github.user.url}")
    private String githubUserUrl;

    @Override
    public String loginByGitee(String state, String code, HttpServletRequest request) {
        //防止csrf攻击
        if (!giteeState.equals(state)) {
            throw new UnsupportedOperationException("state不匹配");
        }
        try {
            String tokenUrl = giteeTokenUrl.concat(code);
            String result = HttpUtil.post(tokenUrl, "");
            String accessToken = (String) JSONUtil.parseObj(result).get("access_token");
            // 请求用户信息
            String userUrl = giteeUserUrl.concat(accessToken);
            JSONObject userInfo = JSONUtil.parseObj(HttpUtil.get(userUrl));
            String name = (String) userInfo.get("name");
            String avatarUrl = (String) userInfo.get("avatar_url");
            return autoRegister3rdAndLogin(name, avatarUrl, request);
        } catch (Exception e) {
            return "rediect:http://localhost:8000/user/login?code=500";
        }
    }


    @Override
    public String loginByGithub(String state, String code, HttpServletRequest request) {

        if (!githubState.equals(state)) {
            throw new UnsupportedOperationException("state不匹配");
        }
        try {
            String tokenUrl = githubTokenUrl.concat(code);

            String result = HttpUtil.post(tokenUrl, "", 3000);
            Map<String, String> resultMap = splitGithubAccessToken(result);
            String accessToken = resultMap.get("access_token");
            log.info("github 返回值:{}", result);
            // 请求用户信息
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Bearer ".concat(accessToken));
            String userInfo = HttpUtil.createGet(githubUserUrl).addHeaders(headerMap).execute(false).body();
            log.info("github 返回userInfo:{}", userInfo);
            String name = (String) JSONUtil.parseObj(userInfo).get("login");
            String avatarUrl = (String) JSONUtil.parseObj(userInfo).get("avatar_url");
            return autoRegister3rdAndLogin(name, avatarUrl, request);
        } catch (Exception e) {
            return "redirect:http://localhost:8000/user/login?code=500";
        }


    }

    private String autoRegister3rdAndLogin(String name, String avatar, HttpServletRequest request) {
        // 邮箱快速登录(方法内已经包含了注册的逻辑)
        String defaultPassword = "123456789";

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userName", name);
        userQueryWrapper.eq("isDelete", 0);
        User user = getOne(userQueryWrapper);
        if (user != null) {
            //记录用户的登录态
            request.getSession().setAttribute(USER_LOGIN_STATE, user);
            return "redirect:http://localhost:8000/user/login?code=200";
        } else {
            long registerId = super.userRegister(name, defaultPassword, defaultPassword);
            User user1 = new User();
            user1.setId(registerId);
            user1.setUserAvatar(avatar);
            boolean updateById = super.updateById(user1);
            ThrowUtils.throwIf(!updateById,ErrorCode.SYSTEM_ERROR);
            if (registerId >= 0) {
                return "redirect:http://localhost:8000/";
            }
        }


        return "rediect:http://localhost:8000/user/login";
    }

    private Map<String, String> splitGithubAccessToken(String data) {
        Map<String, String> result = new HashMap<>();
        Arrays.stream(data.split("&"))
                .forEach(entry -> {
                    String[] keyValue = entry.split("=");
                    if (keyValue.length == 2) {
                        result.put(keyValue[0], keyValue[1]);
                    }
                });
        System.out.println(result);
        return result;
    }

}
