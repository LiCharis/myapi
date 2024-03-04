package com.my.myapicommon.common;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义错误码
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    BLACK_LIST_ERROR(40401,"您已被列入黑名单，禁止调用"),
    ACCESS_KEY_ERROR(40402,"您的accessKey错误，请联系管理员处理"),
    RANDOM_NUMBER_ERROR(40403,"请求随机数错误,禁止消息重放"),
    SECRET_KEY_ERROR(40404,"接口调用认证失败,请联系管理员处理"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),
    INTERFACE_LEFTNUM_RUNOUT(50002,"该接口的调用资源已用尽"),
    USER_INTERFACE_LEFTNUM_RUNOUT(50003,"您对于该接口的剩余调用次数已用尽"),
    INVOKE_TIMEOUT(50004,"调用超时"),
    PREFIX_NULL(50005,"重复提交前缀为空"),
    IDEMPOTENT_ERROR(50006,"请求重复"),
    TOO_MANY_REQUEST(50007,"请求过于频繁");


    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static List<String> getMessages(){
        return Arrays.stream(values()).map(item-> item.message).collect(Collectors.toList());
    }

    /**
     * 根据 message 获取枚举
     *
     * @return
     */
    public static ErrorCode getEnumByMessage(String message) {
        if (ObjectUtils.isEmpty(message)) {
            return null;
        }
        for (ErrorCode anEnum : ErrorCode.values()) {
            if (anEnum.message.equals(message)) {
                return anEnum;
            }
        }
        return null;
    }

}
