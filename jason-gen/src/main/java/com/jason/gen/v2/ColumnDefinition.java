package com.jason.gen.v2;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/15
 **/
@Data
public class ColumnDefinition implements Serializable {
    /**
     * 主键标识
     */
    @JpNotNull(message = "主键标识为空")
    private Boolean keyFlag;
    /**
     * 列名称
     */
    @JpNotNull(message = "列名称为空")
    private String columnName;
    /**
     * 过滤前缀后的列
     */
    private String filterColumnName;
    /**
     * 列注释
     */
    @JpNotNull(message = "列注释为空")
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
    @JpNotNull(message = "jdbc类型名称为空")
    private String jdbcTypeName;
    /**
     * java属性名称
     */
    @JpNotNull(message = "java属性名称为空")
    private String javaFieldName;
    /**
     * 是否为枚举类型
     */
    private Boolean enumType;
    /**
     * java类型名称
     */
    @JpNotNull(message = "java类型名称为空")
    private String javaTypeName;
    /**
     * java类型包路径
     */
    @JpNotNull(message = "java类型包路径为空")
    private String javaTypePackage;
    /**
     * 是否能为空
     */
    private Boolean nullable;
    /**
     * 是否自增
     */
    private Boolean autoincrement;
    /**
     * 枚举定义数组
     */
    private List<EnumDefinition> enumDefinitionList;
}
