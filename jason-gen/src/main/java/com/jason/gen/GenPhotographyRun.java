package com.jason.gen;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.jason.gen.config.GenFieldEnumHandler;
import com.jason.gen.constant.GenConstant;
import com.jason.gen.entity.GenConfigData;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 摄影展系统代码生成（3.5.3.1版本）
 * 注意： 请先修改resources目录下的genPhotography.properties文件中的配置，无误则执行当前类下的main方法
 * 开发文档: https://baomidou.com/pages/981406/#%E6%95%B0%E6%8D%AE%E5%BA%93%E9%85%8D%E7%BD%AE-datasourceconfig
 *
 * @author guozhongcheng
 * @since 2023/6/8 16:58
 */
@SuppressWarnings("all")
public class GenPhotographyRun {


    /**
     * 代码生成入口
     * 注意： 请先修改resources目录下的genPhotography.properties文件中的配置
     * 通过 {@link GenPhotographyRun#buildPackageConfig} 方法去自定义构建哪些类
     */
    public static void main(String[] args) throws Exception {
        System.out.println("====================================开始执行代码生成====================================");
        doGenerator();
        System.out.println("====================================结束执行代码生成====================================");
    }


    private static Properties properties = new Properties();
    public static GenConfigData GEN_CONFIG_DATA;
    public static String CURRENT_MODULES;

    static {
        // 读取resources目录下的配置文件
        InputStream inputStream = GenPhotographyRun.class.getClassLoader().getResourceAsStream("genPhotography.properties");
        try {
            properties.load(IoUtil.getReader(inputStream, Charset.defaultCharset()));
            // 处理配置信息
            handlerProperties();
            // 构建配置对象
            GEN_CONFIG_DATA = GenConfigData.build(properties);
//            System.out.println(JSONUtil.toJsonPrettyStr(GEN_CONFIG_DATA));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 代码生成实现
     */
    private static void doGenerator() throws Exception {
        List<GenConfigData.ModuleConfig> moduleConfigList = GEN_CONFIG_DATA.getModuleConfigList();
        for (GenConfigData.ModuleConfig item : moduleConfigList) {
            System.out.println("开始生成" + item.getModule() + "模块的文件");
            CURRENT_MODULES = item.getModule();
            // 构建代码生成器类
            AutoGenerator mpg = buildGenerator();
            // 构建全局配置类
            mpg.global(buildGlobalConfig(item));
            // 构建包配置类
            mpg.packageInfo(buildPackageConfig(item));
            // 构建模板配置类
            mpg.template(buildTemplateConfig(item));
            // 构建策略配置类
            mpg.strategy(buildStrategyConfig(item));
            // 构建自定义ftl占位参数
            mpg.injection(bulidInjectionConfig(item));
            // 开始生成代码文件
            mpg.execute(new FreemarkerTemplateEngine());
            System.out.println("生成" + item.getModule() + "模块的文件成功");
        }
    }

    /**
     * 构建自定义占位值
     *
     * @param moduleConfig 模块配置对象
     * @return 配置类
     * @throws Exception
     */
    private static InjectionConfig bulidInjectionConfig(GenConfigData.ModuleConfig moduleConfig) throws Exception {
        InjectionConfig.Builder builder = new InjectionConfig.Builder();
        Map<String, Object> customMap = new HashMap<>(16);
        if (GenConstant.API.equals(moduleConfig.getModule())) {
            // 首字母大写
            customMap.put("serviceClassName", GenConstant.API.substring(0, 1).toUpperCase() + GenConstant.API.substring(1));
        } else if (GenConstant.ADMIN.equals(moduleConfig.getModule())) {
            customMap.put("serviceClassName", "");
        } else if (GenConstant.DAO.equals(moduleConfig.getModule())) {
            customMap.put("serviceClassName", "");
        } else {
            throw new Exception("类型不匹配");
        }
        builder.customMap(customMap);
        return builder.build();
    }


    /**
     * 构建模板配置类
     *
     * @param moduleConfig 模块配置对象
     * @return 配置类
     * @throws Exception
     */
    private static TemplateConfig buildTemplateConfig(GenConfigData.ModuleConfig moduleConfig) throws Exception {
        String absolutePath = File.separator + "templates";
        String entityTempPath = absolutePath + File.separator + "EntityP";
        String controllerTempPath = absolutePath + File.separator + "ControllerP";
        String serviceTempPath = absolutePath + File.separator + "ServiceP";
        String serviceImplTempPath = absolutePath + File.separator + "ServiceImplP";
        String mapperTempPath = absolutePath + File.separator + "MapperP";
        String mapperXmlTempPath = absolutePath + File.separator + "MapperXmlP";
        TemplateConfig.Builder builder = new TemplateConfig.Builder();

        if (GenConstant.API.equals(moduleConfig.getModule())) {
            builder.entity(entityTempPath)
                    .controller(controllerTempPath)
                    .service(serviceTempPath)
                    .serviceImpl(serviceImplTempPath)
                    .mapper(mapperTempPath)
                    .xml(mapperXmlTempPath);
        } else if (GenConstant.ADMIN.equals(moduleConfig.getModule())) {
            builder.entity(entityTempPath)
                    .controller(controllerTempPath)
                    .service(serviceTempPath)
                    .serviceImpl(serviceImplTempPath)
                    .mapper(mapperTempPath)
                    .xml(mapperXmlTempPath);
        } else if (GenConstant.DAO.equals(moduleConfig.getModule())) {
            builder.entity(entityTempPath)
                    .controller(controllerTempPath)
                    .service(serviceTempPath)
                    .serviceImpl(serviceImplTempPath)
                    .mapper(mapperTempPath)
                    .xml(mapperXmlTempPath);
        } else {
            throw new Exception("类型不匹配");
        }
        // 构建模板配置类
        return builder.build();
    }

    /**
     * 构建代码生成器类
     *
     * @return AutoGenerator
     */
    private static AutoGenerator buildGenerator() {
        // 建立数据库连接
        DataSourceConfig dsc = new DataSourceConfig.Builder(
                GEN_CONFIG_DATA.getDbUrl(),
                GEN_CONFIG_DATA.getDbUserName(),
                GEN_CONFIG_DATA.getDbPassWord())
                // 自定义字段类型映射
                .typeConvertHandler(new GenFieldEnumHandler())
                .build();
        // 构建代码生成器类
        return new AutoGenerator(dsc);
    }

    /**
     * 构建全局配置类
     *
     * @param moduleConfig 模块配置对象
     * @return 配置类
     * @throws Exception
     */
    private static GlobalConfig buildGlobalConfig(GenConfigData.ModuleConfig moduleConfig) {
        // 作者名称
        String author = GEN_CONFIG_DATA.getAuthor();
        // 全局配置
        GlobalConfig.Builder globalConfigBuilder = new GlobalConfig
                .Builder()
                .outputDir(GEN_CONFIG_DATA.getProjectPath() + "/" + moduleConfig.getModuleName() + "/src/main/java")
                .author(author)
                // 禁止打开目录
                .disableOpenDir()
                .commentDate("yyyy-MM-dd");
        // 构建全局配置类
        return globalConfigBuilder.build();
    }

    /**
     * 构建包配置类
     *
     * @param moduleConfig 模块配置对象
     * @return 配置类
     * @throws Exception
     */
    private static PackageConfig buildPackageConfig(GenConfigData.ModuleConfig moduleConfig) throws Exception {
        // 构建包配置类
        PackageConfig.Builder builder = new PackageConfig.Builder();
        builder.parent(moduleConfig.getPackagePath())
                .controller("controller")
                .service("service")
                .serviceImpl("service.impl")
                .entity("entity.po")
                .mapper("mapper")
                .xml("mapper");
        buildGenFile(builder, moduleConfig);
        return builder.build();
    }

    /**
     * 控制生成哪些文件
     */
    private static void buildGenFile(PackageConfig.Builder builder, GenConfigData.ModuleConfig moduleConfig) throws Exception {
        Map<OutputFile, String> pathInfo = new HashMap<>(5);
        if (!moduleConfig.getGenEntity()) {
            pathInfo.put(OutputFile.entity, "");
        }
        if (!moduleConfig.getGenController()) {
            pathInfo.put(OutputFile.controller, "");
        }
        if (!moduleConfig.getGenService()) {
            pathInfo.put(OutputFile.service, "");
        }
        if (!moduleConfig.getGenServiceImpl()) {
            pathInfo.put(OutputFile.serviceImpl, "");
        }
        if (!moduleConfig.getGenMapper()) {
            pathInfo.put(OutputFile.mapper, "");
        }
        if (!moduleConfig.getGenMapperXml()) {
            pathInfo.put(OutputFile.xml, "");
        } else {
            pathInfo.put(OutputFile.xml,
                    GEN_CONFIG_DATA.getProjectPath() + "/" + moduleConfig.getModuleName() + "/src/main/resources/mapper/");
        }
        builder.pathInfo(pathInfo);
    }

    /**
     * 构建策略配置类，在此方法中控制生成哪些类
     *
     * @param moduleConfig 模块配置对象
     * @return 配置类
     * @throws Exception
     */
    private static StrategyConfig buildStrategyConfig(GenConfigData.ModuleConfig moduleConfig) throws Exception {
        // 构建策略配置类
        StrategyConfig.Builder builder = new StrategyConfig.Builder();
        builder
                // 添加需要生成的表
                .addInclude(GEN_CONFIG_DATA.getTableArr())
                // 设置过滤表前缀
                .addTablePrefix(GenConstant.FILTER_TABLE_PREFIX);

        if (GenConstant.API.equals(moduleConfig.getModule())) {
            builder.controllerBuilder()
                    .enableFileOverride()
                    .convertFileName((entityName -> "Api" + entityName + ConstVal.CONTROLLER))
                    .serviceBuilder()
                    .enableFileOverride()
                    .convertServiceFileName((entityName -> "Api" + entityName + ConstVal.SERVICE))
                    .convertServiceImplFileName((entityName -> "Api" + entityName + ConstVal.SERVICE_IMPL));
        } else if (GenConstant.ADMIN.equals(moduleConfig.getModule())) {
            builder.controllerBuilder()
                    .enableFileOverride()
                    .convertFileName((entityName -> "" + entityName + ConstVal.CONTROLLER))
                    .serviceBuilder()
                    .enableFileOverride()
                    .convertServiceFileName((entityName -> "" + entityName + ConstVal.SERVICE))
                    .convertServiceImplFileName((entityName -> "" + entityName + ConstVal.SERVICE_IMPL));
        } else if (GenConstant.DAO.equals(moduleConfig.getModule())) {
            builder.entityBuilder()
                    .naming(NamingStrategy.underline_to_camel)
                    .enableFileOverride()
                    .mapperBuilder()
                    .enableFileOverride();
        } else {
            throw new Exception("类型不匹配");
        }

        return builder.build();
    }

    /**
     * 处理配置信息
     */
    private static void handlerProperties() {
//        System.out.println("读取的配置内容如下:");
        if (properties != null) {
            // 遍历
            properties.forEach((k, v) -> {
                // 字符串首尾去空
                if (v instanceof String) {
                    String v2 = ((String) v).trim();
                    if (k instanceof String) {
                        properties.setProperty((String) k, v2);
                    }
                }
            });
        }
//        System.out.println(JSONUtil.toJsonPrettyStr(properties));
//        System.out.println("==================================");
    }
}
