package com.jason.gen.constant;

import cn.hutool.core.text.StrPool;
import com.jason.gen.enums.ServiceNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 生成器常量池
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
public interface Constant {
    Charset CHARSET = StandardCharsets.UTF_8;
    String CHARSET_STR = StandardCharsets.UTF_8.toString();
    String SEPARATOR = File.separator;
    String JAVA_PATH = "src" + SEPARATOR + "main" + SEPARATOR + "java";
    String RESOURCES_PATH = "src" + SEPARATOR + "main" + SEPARATOR + "resources";
    String JASON_GEN_TEMPLATES_PATH = "jason-gen" + SEPARATOR + RESOURCES_PATH + SEPARATOR + "templates";
    String MODULES_PROJECT_NAME = "jason-modules";
    String CONTROLLER_PACKAGE = "controller";
    String SERVICE_PACKAGE = "service";
    String SERVICE_IMPL_PACKAGE = "service.impl";
    String ENTITY_PACKAGE = "entity.po";
    String MAPPER_PACKAGE = "mapper";
    String MAPPER_XML_PACKAGE = "mapper";
    String ENUM_PACKAGE = "enums.db";

    interface Api {
        ServiceNameEnum SERVICE_NAME = ServiceNameEnum.API;
        String MODULE_PROJECT_NAME = "jason-photography-api";
        String BASE_PACKAGE = "com.jason.photography.api";
        String BASE_PACKAGE_PATH = BASE_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_PACKAGE = MODULES_PROJECT_NAME + SEPARATOR
                + MODULE_PROJECT_NAME + SEPARATOR
                + JAVA_PATH + SEPARATOR
                + BASE_PACKAGE_PATH;
        String FULL_RESOURCE_PACKAGE = MODULES_PROJECT_NAME + SEPARATOR
                + MODULE_PROJECT_NAME + SEPARATOR
                + RESOURCES_PATH;
        String FULL_CONTROLLER_PACKAGE = FULL_PACKAGE + SEPARATOR + CONTROLLER_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_SERVICE_PACKAGE = FULL_PACKAGE + SEPARATOR + SERVICE_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_SERVICE_IMPL_PACKAGE = FULL_PACKAGE + SEPARATOR + SERVICE_IMPL_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_ENTITY_PACKAGE = FULL_PACKAGE + SEPARATOR + ENTITY_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_MAPPER_PACKAGE = FULL_PACKAGE + SEPARATOR + MAPPER_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_MAPPER_XML_PACKAGE = FULL_RESOURCE_PACKAGE + SEPARATOR + MAPPER_XML_PACKAGE;
        String FULL_ENUM_PACKAGE = Dao.FULL_ENUM_PACKAGE;
    }
    @Getter
    @AllArgsConstructor
    @SuppressWarnings("all")
    enum ApiEnum {
        MODULE_PROJECT_NAME(Constant.Api.MODULE_PROJECT_NAME),
        BASE_PACKAGE(Constant.Api.BASE_PACKAGE),
        BASE_PACKAGE_PATH(Constant.Api.BASE_PACKAGE_PATH),
        FULL_PACKAGE(Constant.Api.FULL_PACKAGE),
        FULL_RESOURCE_PACKAGE(Constant.Api.FULL_RESOURCE_PACKAGE),
        FULL_CONTROLLER_PACKAGE(Constant.Api.FULL_CONTROLLER_PACKAGE),
        FULL_SERVICE_PACKAGE(Constant.Api.FULL_SERVICE_PACKAGE),
        FULL_ENTITY_PACKAGE(Constant.Api.FULL_ENTITY_PACKAGE),
        FULL_MAPPER_PACKAGE(Constant.Api.FULL_MAPPER_PACKAGE),
        FULL_MAPPER_XML_PACKAGE(Constant.Api.FULL_MAPPER_XML_PACKAGE),
        FULL_ENUM_PACKAGE(Constant.Api.FULL_ENUM_PACKAGE),
        ;
        private final String value;
    }

    interface Admin {
        ServiceNameEnum SERVICE_NAME = ServiceNameEnum.ADMIN;
        String MODULE_PROJECT_NAME = "jason-photography-admin";
        String BASE_PACKAGE = "com.jason.photography.admin";
        String BASE_PACKAGE_PATH = BASE_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_JAVA_PACKAGE = MODULES_PROJECT_NAME + SEPARATOR
                + MODULE_PROJECT_NAME + SEPARATOR
                + JAVA_PATH + SEPARATOR
                + BASE_PACKAGE_PATH;
        String FULL_RESOURCE_PACKAGE = MODULES_PROJECT_NAME + SEPARATOR
                + MODULE_PROJECT_NAME + SEPARATOR
                + RESOURCES_PATH;
        String FULL_CONTROLLER_PACKAGE = FULL_JAVA_PACKAGE + SEPARATOR + CONTROLLER_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_SERVICE_PACKAGE = FULL_JAVA_PACKAGE + SEPARATOR + SERVICE_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_SERVICE_IMPL_PACKAGE = FULL_JAVA_PACKAGE + SEPARATOR + SERVICE_IMPL_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_ENTITY_PACKAGE = FULL_JAVA_PACKAGE + SEPARATOR + ENTITY_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_MAPPER_PACKAGE = FULL_JAVA_PACKAGE + SEPARATOR + MAPPER_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_MAPPER_XML_PACKAGE = FULL_RESOURCE_PACKAGE + SEPARATOR + MAPPER_XML_PACKAGE;
        String FULL_ENUM_PACKAGE = Dao.FULL_ENUM_PACKAGE;
    }

    interface Dao {
        ServiceNameEnum SERVICE_NAME = ServiceNameEnum.DAO;
        String MODULE_PROJECT_NAME = "jason-photography-dao";
        String BASE_PACKAGE = "com.jason.photography.dao";
        String BASE_PACKAGE_PATH = BASE_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_PACKAGE = MODULES_PROJECT_NAME + SEPARATOR
                + MODULE_PROJECT_NAME + SEPARATOR
                + JAVA_PATH + SEPARATOR
                + BASE_PACKAGE_PATH;
        String FULL_RESOURCE_PACKAGE = MODULES_PROJECT_NAME + SEPARATOR
                + MODULE_PROJECT_NAME + SEPARATOR
                + RESOURCES_PATH;
        String FULL_CONTROLLER_PACKAGE = FULL_PACKAGE + SEPARATOR + CONTROLLER_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_SERVICE_PACKAGE = FULL_PACKAGE + SEPARATOR + SERVICE_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_SERVICE_IMPL_PACKAGE = FULL_PACKAGE + SEPARATOR + SERVICE_IMPL_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_ENTITY_PACKAGE = FULL_PACKAGE + SEPARATOR + ENTITY_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_MAPPER_PACKAGE = FULL_PACKAGE + SEPARATOR + MAPPER_PACKAGE.replace(StrPool.DOT, SEPARATOR);
        String FULL_MAPPER_XML_PACKAGE = FULL_RESOURCE_PACKAGE + SEPARATOR + MAPPER_XML_PACKAGE;
        String FULL_ENUM_PACKAGE = FULL_PACKAGE + SEPARATOR + ENUM_PACKAGE.replace(StrPool.DOT, SEPARATOR);
    }


}
