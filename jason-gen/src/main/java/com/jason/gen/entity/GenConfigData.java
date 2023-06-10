package com.jason.gen.entity;

import cn.hutool.core.text.StrPool;
import com.jason.gen.constant.GenConstant;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 代码生成相关配置数据类
 *
 * @author guozhongcheng
 * @since 2023/6/8
 **/
@Data
public class GenConfigData implements Serializable {

    /**
     * 生成目录
     */
    private String projectPath;
    /**
     * 需要生成的模块数组
     */
    private List<ModuleConfig> moduleConfigList;
    /**
     * 作者
     */
    private String author;
    /**
     * 表名数组
     */
    private String[] tableArr;
    /**
     * 数据库地址
     */
    private String dbUrl;
    /**
     * 数据库用户名
     */
    private String dbUserName;
    /**
     * 数据库用户密码
     */
    private String dbPassWord;


    public static GenConfigData build(Properties properties) throws Exception {
        GenConfigData genConfigData = null;
        if (properties != null) {
            genConfigData = new GenConfigData();
            genConfigData.setProjectPath(properties.getProperty("projectPath"));
            genConfigData.setAuthor(properties.getProperty("author"));
            genConfigData.setDbUrl(properties.getProperty("database.url"));
            genConfigData.setDbUserName(properties.getProperty("database.username"));
            genConfigData.setDbPassWord(properties.getProperty("database.password"));
            // 需要生成的表
            String tables = properties.getProperty("tables");
            String[] tableArr = tables.split(StrPool.COMMA);
            for (int i = 0; i < tableArr.length; i++) {
                tableArr[i] = tableArr[i].trim();
            }
            genConfigData.setTableArr(tableArr);
            // 需要生成的模块
            String modules = properties.getProperty("modules");
            String[] modulesArr = modules.split(StrPool.COMMA);
            List<ModuleConfig> list = new ArrayList<>(3);
            for (int i = 0; i < modulesArr.length; i++) {
                String module = modulesArr[i].trim();
                ModuleConfig moduleConfig;
                if (GenConstant.API.equals(module)) {
                    moduleConfig = buildApiModuleConfig(properties);
                } else if (GenConstant.ADMIN.equals(module)) {
                    moduleConfig = buildAdminModuleConfig(properties);
                } else if (GenConstant.DAO.equals(module)) {
                    moduleConfig = buildDaoModuleConfig(properties);
                } else {
                    throw new Exception("modules类型不匹配");
                }
                list.add(moduleConfig);
            }
            genConfigData.setModuleConfigList(list);

        }
        return genConfigData;
    }


    private static ModuleConfig buildApiModuleConfig(Properties properties) {
        ModuleConfig moduleConfig = new ModuleConfig();
        moduleConfig.setModule(GenConstant.API);
        moduleConfig.setModuleName(GenConstant.PROJECT_MODULES_NAME + File.separator + GenConstant.JASON_PHOTOGRAPHY_API);
        moduleConfig.setPackagePath(GenConstant.JASON_PHOTOGRAPHY_API_PACKAGE);
        moduleConfig.setGenController(true);
        moduleConfig.setGenService(true);
        moduleConfig.setGenServiceImpl(true);
        return moduleConfig;
    }

    private static ModuleConfig buildAdminModuleConfig(Properties properties) {
        ModuleConfig moduleConfig = new ModuleConfig();
        moduleConfig.setModule(GenConstant.ADMIN);
        moduleConfig.setModuleName(GenConstant.PROJECT_MODULES_NAME + File.separator + GenConstant.JASON_PHOTOGRAPHY_ADMIN);
        moduleConfig.setPackagePath(GenConstant.JASON_PHOTOGRAPHY_ADMIN_PACKAGE);
        moduleConfig.setGenController(true);
        moduleConfig.setGenService(true);
        moduleConfig.setGenServiceImpl(true);
        return moduleConfig;
    }

    private static ModuleConfig buildDaoModuleConfig(Properties properties) {
        ModuleConfig moduleConfig = new ModuleConfig();
        moduleConfig.setModule(GenConstant.DAO);
        moduleConfig.setModuleName(GenConstant.PROJECT_MODULES_NAME + File.separator + GenConstant.JASON_PHOTOGRAPHY_DAO);
        moduleConfig.setPackagePath(GenConstant.JASON_PHOTOGRAPHY_DAO_PACKAGE);
        moduleConfig.setGenEntity(true);
        moduleConfig.setGenMapper(true);
        moduleConfig.setGenMapperXml(true);
        return moduleConfig;
    }


    @Data
    public static class ModuleConfig {
        private String module;
        private String moduleName;
        private String packagePath;
        private Boolean genEntity = false;
        private Boolean genController = false;
        private Boolean genService = false;
        private Boolean genServiceImpl = false;
        private Boolean genMapper = false;
        private Boolean genMapperXml = false;
    }


}
