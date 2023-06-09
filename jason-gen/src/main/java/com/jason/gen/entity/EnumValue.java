package com.jason.gen.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 枚举值对象
 *
 * @author guozhongcheng
 * @since 2023/6/10
 **/
@Data
public class EnumValue implements Serializable {
    /**
     * 枚举名称
     */
    private String enumName;
    /**
     * 枚举值
     */
    private Integer value;
    /**
     * 枚举描述
     */
    private String desc;
}
