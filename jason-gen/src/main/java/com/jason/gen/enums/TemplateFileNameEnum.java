package com.jason.gen.enums;

import java.util.Arrays;

/**
 * 模板文件名称
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
@SuppressWarnings("all")
public enum TemplateFileNameEnum {
    ControllerP,
    ServiceP,
    ServiceImplP,
    EntityP,
    MapperP,
    MapperXmlP,
    EnumP;

    public static TemplateFileNameEnum get(String templateFileName) {
        return Arrays.stream(TemplateFileNameEnum.values()).filter(n -> n.name().equalsIgnoreCase(templateFileName)).findFirst().get();
    }

    /**
     * 获取Java类名称后缀
     *
     * @param templateFileNameEnum 模板文件名称枚举对象
     * @return 后缀
     */
    public static String getJavaClassNameSuffix(TemplateFileNameEnum templateFileNameEnum) {
        if (templateFileNameEnum != null) {
            if (templateFileNameEnum == EntityP) {
                return "";
            }
            String name = templateFileNameEnum.name();
            if (templateFileNameEnum == MapperXmlP) {
                // Mapper
                return name.substring(0, name.length() - 4);
            }
            return name.substring(0, name.length() - 1);
        }
        return "";
    }
}
