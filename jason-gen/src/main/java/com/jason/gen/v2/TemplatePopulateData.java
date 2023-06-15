package com.jason.gen.v2;

import lombok.Data;

/**
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
@Data
public class TemplatePopulateData extends TableDefinition {

    private String author;
    private String date;
    private String packageController;
    private String packageService;
    private String packageServiceImpl;
    private String packageEntity;
    private String packageMapper;
    private String packageMapperXml;
    private String packageEnum;


}
