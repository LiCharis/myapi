package com.my.springbootinit;

import cn.hutool.http.HttpRequest;
import com.my.springbootinit.config.WxOpenConfig;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 主类测试
 *
 
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private WxOpenConfig wxOpenConfig;

    @Test
    void contextLoads() {
        String result = HttpRequest.post("https://github.com/login/oauth/access_token?client_id=81281fded19e878ed5f1&redirect_uri=http://localhost:8101/api/login3rd/github/callback&client_secret=4b7903ab4584b470a8bfaf0c8288f3cb2840eca8&code=15b63c2b153389285f30").execute().body();
        System.out.println(result);
    }


}
