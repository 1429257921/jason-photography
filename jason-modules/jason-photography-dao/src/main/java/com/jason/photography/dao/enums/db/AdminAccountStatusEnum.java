package com.jason.photography.dao.enums.db;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * t_admin_account表账号状态枚举类
 *
 * @author guozhongcheng
 * @since 2023-06-11
 */
@Getter
@AllArgsConstructor
public enum AdminAccountStatusEnum implements IEnum<Integer> {
    /**
     * （0、启用，1、禁用）
     **/
    qy0(0, "启用"),
    jy1(1, "禁用"),
    ;
    @EnumValue
    @JsonValue
    private final int value;
    private final String desc;

    public static AdminAccountStatusEnum getEnum(Integer value) {
        if (value != null) {
            for (AdminAccountStatusEnum valueEnum : AdminAccountStatusEnum.values()) {
                if (valueEnum.getValue() == value) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static AdminAccountStatusEnum getEnum(String desc) {
        if (StrUtil.isNotBlank(desc)) {
            for (AdminAccountStatusEnum valueEnum : AdminAccountStatusEnum.values()) {
                if (valueEnum.getDesc().equals(desc)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static String getDesc(Integer value) {
        AdminAccountStatusEnum enumValue = getEnum(value);
        return enumValue != null ? enumValue.getDesc() : "";
    }

    public static Integer getValue(String desc) {
        AdminAccountStatusEnum enumValue = getEnum(desc);
        return enumValue != null ? enumValue.getValue() : null;
    }
}