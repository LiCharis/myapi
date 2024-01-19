package com.my.springbootinit.model.enums;

import com.my.myapiclientsdk.model.ModelInterfaceInfo;
import com.my.myapiclientsdk.model.User;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ClassEnum {

    STRING("String", String.class),
    LONG("Long", Long.class),
    USER("User", User.class),
    MODELINFO("ModelInfo", ModelInterfaceInfo.class);


    private final String value;

    private final Class aClass;

    ClassEnum(String value, Class aClass) {
        this.value = value;
        this.aClass = aClass;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ClassEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ClassEnum anEnum : ClassEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public Class getaClass() {
        return aClass;
    }


}
