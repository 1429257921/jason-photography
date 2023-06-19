package com.jason.gen.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 模板文件名称
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
@Getter
@AllArgsConstructor
@SuppressWarnings("all")
public enum TemplateFileNameEnum {
    Controller("Controller"),
    Service("Service"),
    ServiceImpl("ServiceImpl"),
    Entity(""),
    Mapper("Mapper"),
    MapperXml("Mapper"),
    Enum("Enum");

    /**
     * 映射Java类名后缀
     */
    private final String javaClassNameSuffix;

    /**
     * 根据模板文件名称获取枚举
     *
     * @param templateFileName 模板文件名称
     * @return 模板文件名称枚举
     */
    public static TemplateFileNameEnum get(String templateFileName) {
        return Arrays.stream(TemplateFileNameEnum.values()).filter(n -> n.name().equalsIgnoreCase(templateFileName)).findFirst().get();
    }

    /**
     * 获取Java类名称后缀
     *
     * @param templateFileNameEnum 模板文件名称枚举对象
     * @return 后缀
     */
    public static String getJavaClassNameSuffix(String templateFileName) throws Exception {
        if (StrUtil.isBlank(templateFileName)) {
            throw new Exception("模板文件名称为空");
        }
        TemplateFileNameEnum templateFileNameEnum = get(templateFileName);
        if (templateFileNameEnum == null) {
            throw new Exception("模板文件名称【" + templateFileName + "】未找到匹配项");
        }
        return templateFileNameEnum.getJavaClassNameSuffix();
    }
}
