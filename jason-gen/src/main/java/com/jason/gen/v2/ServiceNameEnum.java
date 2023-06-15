package com.jason.gen.v2;

import java.util.Arrays;

/**
 * 服务名称枚举类
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
@SuppressWarnings("ALL")
public enum ServiceNameEnum {
    API,
    ADMIN,
    DAO;

    public static ServiceNameEnum get(String param) {
        return Arrays.stream(ServiceNameEnum.values()).filter(n -> n.name().equalsIgnoreCase(param)).findFirst().orElse(null);
    }
}