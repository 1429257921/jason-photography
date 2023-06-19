package com.jason.photography.dao.enums.db;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 删除标志枚举类
 *
 * @author guozhongcheng
 * @since 2023-06-19
 */
@Getter
@AllArgsConstructor
@SuppressWarnings("all")
public enum DelFlagEnum implements IEnum<Integer> {
    /**
     * 删除标志
     **/
    zc0(0, "正常"),
    sc1(1, "删除"),
    ;
    @EnumValue
    @JsonValue
    private final Integer value;
    private final String desc;

    public static DelFlagEnum getEnum(Integer value) {
        return Arrays.stream(DelFlagEnum.values()).filter(valueEnum -> valueEnum.getValue().equals(value)).findFirst().orElse(null);
    }

    public static DelFlagEnum getEnum(String desc) {
        return Arrays.stream(DelFlagEnum.values()).filter(valueEnum -> valueEnum.getDesc().equals(desc)).findFirst().orElse(null);
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