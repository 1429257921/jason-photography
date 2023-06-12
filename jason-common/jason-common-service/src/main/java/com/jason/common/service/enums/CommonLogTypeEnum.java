package com.jason.common.service.enums;

import cn.hutool.core.util.StrUtil;

/**
 * 通用日志类型枚举
 *
 * @author guozhongcheng
 * @since 2023/6/12
 */
public enum CommonLogTypeEnum {
    /**
     * 日志类型
     */
    UNDEFINED_LOG(-1, "未配置日志类型"),
    SYSTEM_LOG(0, "系统日志"),
    ERROR_LOG(1, "错误日志"),
    ;

    private final Integer code;
    private final String desc;

    CommonLogTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static Integer getCode(String desc) {
        if (StrUtil.isNotBlank(desc)) {
            for (CommonLogTypeEnum value : CommonLogTypeEnum.values()) {
                if (value.getDesc().equalsIgnoreCase(desc)) {
                    return value.getCode();
                }
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
