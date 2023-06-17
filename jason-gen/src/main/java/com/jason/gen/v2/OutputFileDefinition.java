package com.jason.gen.v2;

import lombok.Data;

/**
 * TODO
 *
 * @author gzc
 * @since 2023/6/16 2:57
 **/
@Data
public class OutputFileDefinition {
    /**
     * 模板定义
     */
    private TemplateDefinition templateDefinition;
    /**
     * 输出文件基础路径
     */
    private String baseOutputFilePath;
    /**
     * 输出文件名称（文件名称）
     */
    private String outputFileName;
    /**
     * 输出文件名称（文件名称+小数点+文件扩展名）
     */
    private String fullOutputFileName;
    /**
     * 模板文件填充数据
     */
    private TemplatePopulateData populateData;
}
