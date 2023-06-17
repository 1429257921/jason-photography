package com.jason.gen.v2;

import cn.hutool.core.text.CharSequenceUtil;

import java.util.Arrays;

/**
 * TODO
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

    public static String getJavaClassNameSuffix(TemplateFileNameEnum templateFileNameEnum) {
        if (templateFileNameEnum != null) {
            if (templateFileNameEnum == EntityP) {
                return "";
            }
            String name = templateFileNameEnum.name();
            if (templateFileNameEnum == MapperXmlP) {
                return name.substring(0, name.length() - 4);
            }
            return name.substring(0, name.length() - 1);
        }
        return "";
    }
}
