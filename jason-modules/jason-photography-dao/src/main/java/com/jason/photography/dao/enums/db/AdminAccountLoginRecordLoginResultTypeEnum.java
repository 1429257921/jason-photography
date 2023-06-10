package com.jason.photography.dao.enums.db;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * t_admin_account_login_record表登录结果类型枚举类
 *
 * @author guozhongcheng
 * @since 2023-06-11
 */
@Getter
@AllArgsConstructor
public enum AdminAccountLoginRecordLoginResultTypeEnum implements IEnum<Integer> {
    /**
     * （0、登录成功，1、登出成功，2、登录失败，3、登出失败）
     **/
    dlcg0(0, "登录成功"),
    dccg1(1, "登出成功"),
    dlsb2(2, "登录失败"),
    dcsb3(3, "登出失败"),
    ;
    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    public static AdminAccountLoginRecordLoginResultTypeEnum getEnum(Integer value) {
        if (value != null) {
            for (AdminAccountLoginRecordLoginResultTypeEnum valueEnum : AdminAccountLoginRecordLoginResultTypeEnum.values()) {
                if (valueEnum.getValue().equals(value)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static AdminAccountLoginRecordLoginResultTypeEnum getEnum(String desc) {
        if (StrUtil.isNotBlank(desc)) {
            for (AdminAccountLoginRecordLoginResultTypeEnum valueEnum : AdminAccountLoginRecordLoginResultTypeEnum.values()) {
                if (valueEnum.getDesc().equals(desc)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static String getDesc(Integer value) {
        AdminAccountLoginRecordLoginResultTypeEnum enumValue = getEnum(value);
        return enumValue != null ? enumValue.getDesc() : "";
    }

    public static Integer getValue(String desc) {
        AdminAccountLoginRecordLoginResultTypeEnum enumValue = getEnum(desc);
        return enumValue != null ? enumValue.getValue() : null;
    }
}