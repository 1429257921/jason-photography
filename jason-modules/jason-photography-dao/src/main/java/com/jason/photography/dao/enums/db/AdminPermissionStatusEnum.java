package com.jason.photography.dao.enums.db;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * t_admin_permission表状态枚举类
 *
 * @author guozhongcheng
 * @since 2023-06-11
 */
@Getter
@AllArgsConstructor
public enum AdminPermissionStatusEnum implements IEnum<Integer> {
    /**
     * （0、正常，1、禁用）
     **/
    zc0(0, "正常"),
    jy1(1, "禁用"),
    ;
    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    public static AdminPermissionStatusEnum getEnum(Integer value) {
        if (value != null) {
            for (AdminPermissionStatusEnum valueEnum : AdminPermissionStatusEnum.values()) {
                if (valueEnum.getValue().equals(value)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static AdminPermissionStatusEnum getEnum(String desc) {
        if (StrUtil.isNotBlank(desc)) {
            for (AdminPermissionStatusEnum valueEnum : AdminPermissionStatusEnum.values()) {
                if (valueEnum.getDesc().equals(desc)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static String getDesc(Integer value) {
        AdminPermissionStatusEnum enumValue = getEnum(value);
        return enumValue != null ? enumValue.getDesc() : "";
    }

    public static Integer getValue(String desc) {
        AdminPermissionStatusEnum enumValue = getEnum(desc);
        return enumValue != null ? enumValue.getValue() : null;
    }
}