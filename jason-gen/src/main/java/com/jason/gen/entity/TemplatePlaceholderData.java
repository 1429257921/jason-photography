package com.jason.gen.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模板文件占位符数据，主要用于填充模板文件中的${xxx}信息
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplatePlaceholderData extends TableDefinition {
    /**
     * 模块名称
     */
    private String moduleName;
    /**
     * 作者名称
     */
    private String author;
    /**
     * 日期
     */
    private String date;
    /**
     * 文件的包路径
     */
    private String packageController;
    private String packageService;
    private String packageServiceImpl;
    private String packageEntity;
    private String packageMapper;
    private String packageMapperXml;
    private String packageEnum;
    /**
     * 文件的名称
     */
    private String controllerClassName;
    private String serviceClassName;
    private String serviceImplClassName;
    private String entityClassName;
    private String mapperClassName;
    private String mapperXmlClassName;
    private String enumClassName;
    /**
     * 枚举列定义信息（当前文件不为枚举则该对象为空）
     */
    private ColumnDefinition enumColumnDefinition;
}
