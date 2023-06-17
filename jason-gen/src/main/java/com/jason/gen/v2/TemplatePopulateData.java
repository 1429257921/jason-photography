package com.jason.gen.v2;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplatePopulateData extends TableDefinition {

    private String moduleName;
    private String author;
    private String date;

    private String packageController;
    private String packageService;
    private String packageServiceImpl;
    private String packageEntity;
    private String packageMapper;
    private String packageMapperXml;
    private String packageEnum;

    private String controllerClassName;
    private String serviceClassName;
    private String serviceImplClassName;
    private String entityClassName;
    private String mapperClassName;
    private String mapperXmlClassName;
    private String enumClassName;

    private ColumnDefinition enumColumnDefinition;
}
