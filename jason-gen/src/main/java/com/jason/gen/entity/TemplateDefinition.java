package com.jason.gen.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 模板定义信息
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
     * 类名后缀
     */
    private String javaClassNameSuffix;
}
