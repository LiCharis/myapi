package com.my.springbootinit.common;

import com.my.myapicommon.model.User;

public class TypeUtils {

    public static <T> ContentBody<T> createInstance(T input) {
        ContentBody<T> instance = new ContentBody<>();
        return instance;
    }

    public static void main(String[] args) throws NoSuchFieldException {
        String condition = "user"; // 可以根据条件动态确定类型
        ContentBody<?> contentBody;

        if ("string".equals(condition)) {
            contentBody = createInstance("Hello, World!");
        } else if ("user".equals(condition)) {
            User user = new User();
            user.setUserName("lili");
            contentBody = createInstance(user);
        } else {
            // 默认情况
            contentBody = createInstance("Default Value");
        }
        System.out.println(contentBody.getClass().getDeclaredField("data").getType());
    }
}

