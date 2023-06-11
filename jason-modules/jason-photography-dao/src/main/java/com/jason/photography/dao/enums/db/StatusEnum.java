package com.jason.photography.dao.enums.db;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * t_admin_account表启用状态枚举类
 *
 * @author guozhongcheng
 * @since 2023-06-11
 */
@Getter
@AllArgsConstructor
public enum StatusEnum implements IEnum<Integer> {
    /**
     * （0、启用，1、禁用）
     **/
    qy0(0, "启用"),
    jy1(1, "禁用"),
    ;
    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    public static StatusEnum getEnum(Integer value) {
        if (value != null) {
            for (StatusEnum valueEnum : StatusEnum.values()) {
                if (valueEnum.getValue().equals(value)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static StatusEnum getEnum(String desc) {
        if (StrUtil.isNotBlank(desc)) {
            for (StatusEnum valueEnum : StatusEnum.values()) {
                if (valueEnum.getDesc().equals(desc)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static String getDesc(Integer value) {
        StatusEnum enumValue = getEnum(value);
        return enumValue != null ? enumValue.getDesc() : "";
    }

    public static Integer getValue(String desc) {
        StatusEnum enumValue = getEnum(desc);
        return enumValue != null ? enumValue.getValue() : null;
    }
}