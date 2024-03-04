package com.my.springbootinit.aop;

import com.my.myapicommon.common.ErrorCode;
import com.my.myapicommon.model.User;
import com.my.myapicommon.common.ErrorCode;
import com.my.springbootinit.common.ResultUtils;
import com.my.springbootinit.service.UserService;
import com.my.springbootinit.utils.RequestKeyGenerateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.my.springbootinit.annotation.IdempotentCheck;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class IdempotentInterceptor {


    @Resource
    private RedissonClient redissonClient;


    @Around("@annotation(com.my.springbootinit.annotation.IdempotentCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {

//        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
//        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//        String requestId = request.getHeader("requestId");
//        if (StringUtils.isBlank(requestId)){
//            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"未携带请求id");
//        }
//        String requestIdValue = (String) redisTemplate.opsForValue().get(requestId);
//        if (StringUtils.isBlank(requestIdValue)){
//            redisTemplate.opsForValue().set(requestId,requestId,100, TimeUnit.SECONDS);
//        }else {
//            return ResultUtils.error(ErrorCode.IDEMPOTENT_ERROR);
//        }
//        Object result = joinPoint.proceed();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        IdempotentCheck idempotentCheck = method.getAnnotation(IdempotentCheck.class);
        if (StringUtils.isEmpty(idempotentCheck.prefix())) {
            return ResultUtils.error(ErrorCode.PREFIX_NULL);
        }
        //获取自定义key
        final String lockKey = RequestKeyGenerateUtils.getLockKey(joinPoint);
        // 使用Redisson分布式锁的方式判断是否重复提交
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;
        try {
            //尝试抢占锁
            isLocked = lock.tryLock();
            if (!isLocked) {
                return ResultUtils.error(ErrorCode.IDEMPOTENT_ERROR);
            }
            //拿到锁之后设置过期时间
            lock.lock(idempotentCheck.expireTime(), idempotentCheck.timeUnit());

            //方法继续执行
            return joinPoint.proceed();
        } finally {
            //释放锁
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }


}
