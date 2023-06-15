package com.jason.gen.v2;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/13
 **/
@Data
public class TypeConvertDefinition implements Serializable {
    /**
     * 数据库列类型名称
     */
    private String jdbcTypeName;
    /**
     * Java类型名称
     */
    private String javaTypeName;
    /**
     * Java类型包路径
     */
    private String javaTypePackage;
}
