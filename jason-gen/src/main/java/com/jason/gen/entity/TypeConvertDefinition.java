package com.jason.gen.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据库列类型映射Java类型定义信息，主要作用是保存type-converter.properties配置文件的信息
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
