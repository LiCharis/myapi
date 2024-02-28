package com.my.springbootinit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD,ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdempotentCheck {

    /**
     * 锁前缀
     */
    String prefix() default "";

    /**
     * 分隔符
     */
    String divider() default "&";

    /**
     * 锁的过期时间
     * @return
     */
    int expireTime() default 5;

    /**
     * 时间单位
     * @return
     */
    java.util.concurrent.TimeUnit timeUnit() default TimeUnit.SECONDS;


}
