package com.jason.gen.v2;

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
}
