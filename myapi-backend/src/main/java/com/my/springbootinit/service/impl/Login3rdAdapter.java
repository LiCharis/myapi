package com.my.springbootinit.service.impl;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.myapicommon.model.User;
import com.my.myapicommon.common.ErrorCode;
import com.my.springbootinit.common.BaseResponse;
import com.my.springbootinit.common.ResultUtils;
import com.my.springbootinit.common.ThirdLoginRequest;
import com.my.springbootinit.exception.BusinessException;
import com.my.springbootinit.exception.ThrowUtils;
import com.my.springbootinit.model.vo.LoginUserVO;
import com.my.springbootinit.service.Login3rdTarget;
import com.my.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

            /**
             * 这里拿到accessToken后将其重定向到前端，前端再拿着这个token获取到用户信息，然后再执行登录操作
             */
            return "redirect:https://api.liproject.top/user/thirdLogin?access_token=" + accessToken;

//            // 请求用户信息
//            String userUrl = giteeUserUrl.concat(accessToken);
//            JSONObject userInfo = JSONUtil.parseObj(HttpUtil.get(userUrl));
//            String name = (String) userInfo.get("name");
//            String avatarUrl = (String) userInfo.get("avatar_url");
//            return autoRegister3rdAndLogin(name, avatarUrl, request);

        } catch (Exception e) {
            return "redirect:https://api.liproject.top/user/thirdLogin";
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
            return "redirect:https://api.liproject.top/user/thirdLogin?access_token=" + accessToken;
//            // 请求用户信息
//            HashMap<String, String> headerMap = new HashMap<>();
//            headerMap.put("Authorization", "Bearer ".concat(accessToken));
//            String userInfo = HttpUtil.createGet(githubUserUrl).addHeaders(headerMap).execute(false).body();
//            log.info("github 返回userInfo:{}", userInfo);
//            String name = (String) JSONUtil.parseObj(userInfo).get("login");
//            String avatarUrl = (String) JSONUtil.parseObj(userInfo).get("avatar_url");
//            return null;
        } catch (Exception e) {
            return "redirect:https://api.liproject.top/user/thirdLogin";
        }


    }

    @Override
    public BaseResponse<LoginUserVO> autoRegister3rdAndLogin(ThirdLoginRequest thirdLoginRequest, HttpServletRequest request) {

        String name = thirdLoginRequest.getName();
        String avatar = thirdLoginRequest.getAvatar_url();
        if (StringUtils.isAnyBlank(name,avatar)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"第三方用户参数错误");
        }

        // 邮箱快速登录(方法内已经包含了注册的逻辑)
        String defaultPassword = "123456789";

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userName", name);
        userQueryWrapper.eq("isDelete", 0);
        User user = getOne(userQueryWrapper);

        if (user != null) {
            //记录用户的登录态
            request.getSession().setAttribute(USER_LOGIN_STATE, user);
            return ResultUtils.success(super.getLoginUserVO(user));
        }
        //注册
        long registerId = super.userRegister(name, defaultPassword, defaultPassword);
        User user1 = new User();
        user1.setId(registerId);
        user1.setUserAvatar(avatar);
        boolean updateById = super.updateById(user1);
        ThrowUtils.throwIf(!updateById, ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success(super.getLoginUserVO(user1));


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
