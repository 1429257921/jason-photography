package com.jason.photography.dao.enums.db;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * t_admin_account表删除标志枚举类
 *
 * @author guozhongcheng
 * @since 2023-06-11
 */
@Getter
@AllArgsConstructor
public enum DelFlagEnum implements IEnum<Integer> {
    /**
     * （0、正常，1、删除）
     **/
    zc0(0, "正常"),
    sc1(1, "删除"),
    ;
    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    public static DelFlagEnum getEnum(Integer value) {
        if (value != null) {
            for (DelFlagEnum valueEnum : DelFlagEnum.values()) {
                if (valueEnum.getValue().equals(value)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static DelFlagEnum getEnum(String desc) {
        if (StrUtil.isNotBlank(desc)) {
            for (DelFlagEnum valueEnum : DelFlagEnum.values()) {
                if (valueEnum.getDesc().equals(desc)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static String getDesc(Integer value) {
        DelFlagEnum enumValue = getEnum(value);
        return enumValue != null ? enumValue.getDesc() : "";
    }

    public static Integer getValue(String desc) {
        DelFlagEnum enumValue = getEnum(desc);
        return enumValue != null ? enumValue.getValue() : null;
    }
}