package com.my.springbootinit.service.impl.inner;

import com.my.myapicommon.service.InnerNonceService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


@DubboService
public class InnerNonceServiceImpl implements InnerNonceService {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Boolean nonceJudge(String nonce) {
        Object o = redisTemplate.opsForValue().get(nonce);
        if (o == null){
            redisTemplate.opsForValue().set(nonce,nonce,5 ,TimeUnit.MINUTES);
            return true;
        }
        return false;

    }
}
