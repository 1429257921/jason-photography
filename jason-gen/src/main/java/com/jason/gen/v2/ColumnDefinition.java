package com.jason.gen.v2;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/15
 **/
@Data
public class ColumnDefinition implements Serializable {
    /**
     * 列名称
     */
    private String columnName;
    /**
     * 列注释
     */
    private String columnComment;
    /**
     * 列类型长度
     */
    private Integer length;
    /**
     * 刻度
     */
    private Integer scale;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * jdbc类型名称
     */
    private String jdbcTypeName;
    /**
     * java类型名称
     */
    private String javaTypeName;
    /**
     * java类型包路径
     */
    private String javaTypePackage;
    /**
     * 是否能为空
     */
    private Boolean nullable;
}
