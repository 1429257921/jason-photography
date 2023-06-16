package com.jason.gen.v2;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
@Data
public class TemplateDefinition implements Serializable {
    /**
     * 模板文件存储的基础路径
     */
    private String baseTemplateFilePath;
    /**
     * 模板文件名称
     */
    private String templateFileName;
    /**
     * 模板文件名称（文件名称+小数点+文件扩展名）
     */
    private String fullTemplateFileName;
    /**
     * 输出文件基础路径
     */
    private String baseOutputFilePath;
    /**
     * 输出文件名称（文件名称+小数点+文件扩展名）
     */
    private String fullOutputFileName;
    /**
     * 输出文件名称（文件名称）
     */
    private String outputFileName;
    /**
     * 模板文件填充数据（核心）
     */
    private TemplatePopulateData templatePopulateData;
}
