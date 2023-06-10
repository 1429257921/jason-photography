package com.jason.gen.constant;

import java.io.File;

/**
 * 代码生成相关常量配置
 *
 * @author guozhongcheng
 * @since 2023/6/8
 **/
public interface GenConstant {
    String SEPARATOR = File.separator;

    String API = "api";
    String ADMIN = "admin";
    String DAO = "dao";
    String[] FILTER_TABLE_PREFIX = {"t", "jp", "k"};

    String PROJECT_MODULES_NAME = "jason-modules";
    String JAVA_PATH = "src" + SEPARATOR + "main" + SEPARATOR + "java";
    String RESOURCES_PATH = "src" + SEPARATOR + "main" + SEPARATOR + "resources";

    String JASON_PHOTOGRAPHY_DAO = "jason-photography-dao";
    String JASON_PHOTOGRAPHY_DAO_PACKAGE = "com.jason.photography.dao";
    String JASON_PHOTOGRAPHY_DAO_PACKAGE_PATH = "com.jason.photography.dao".replace(".", SEPARATOR);
    String JASON_PHOTOGRAPHY_DAO_ENUM_PACKAGE = JASON_PHOTOGRAPHY_DAO_PACKAGE + ".enums.db";
    String JASON_PHOTOGRAPHY_DAO_ENUM_PACKAGE_PATH = JASON_PHOTOGRAPHY_DAO_ENUM_PACKAGE.replace(".", SEPARATOR);

    String JASON_PHOTOGRAPHY_API = "jason-photography-api";
    String JASON_PHOTOGRAPHY_API_PACKAGE = "com.jason.photography.api";

    String JASON_PHOTOGRAPHY_ADMIN = "jason-photography-admin";
    String JASON_PHOTOGRAPHY_ADMIN_PACKAGE = "com.jason.photography.admin";

    String JASON_GEN_TEMPLATES_PATH = "jason-gen" + SEPARATOR + RESOURCES_PATH + SEPARATOR + "templates";
}
