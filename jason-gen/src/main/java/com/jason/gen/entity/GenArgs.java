package com.jason.gen.entity;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import com.jason.gen.constant.Constant;
import com.jason.gen.enums.ServiceNameEnum;
import com.jason.gen.enums.TemplateFileNameEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 生成器参数配置，主要用于映射genPhotography.properties配置文件的配置信息
 *
 * @author guozhongcheng
 * @since 2023/6/13
 **/
@Data
public class GenArgs implements Serializable {
    /**
     * 项目基础路径
     */
    private String projectPath;
    /**
     * 需要生成的模块名称（多个用逗号分割）
     */
    private String modules;
    /**
     * 作者名称
     */
    private String author;
    /**
     * 需要生成的表名称（多个用逗号分割）
     */
    private String tables;
    /**
     * 需要过滤的表名称前缀（多个用逗号分割）
     */
    private String filterTableNamePrefix;
    /**
     * 需要过滤的列名称前缀（多个用逗号分割）
     */
    private String filterColumnNamePrefix;
    /**
     * 是否覆盖原文件
     */
    private Boolean overwriteFile;
    /**
     * 数据库驱动类包路径
     */
    private String databaseDriverClassName;
    /**
     * 数据库连接URL
     */
    private String databaseUrl;
    /**
     * 数据库账号
     */
    private String databaseUsername;
    /**
     * 数据库密码
     */
    private String databasePassword;
    /**
     * 需要生成Controller类的模块名称（多个用逗号分割）
     */
    private String genController;
    /**
     * 需要生成Service类的模块名称（多个用逗号分割）
     */
    private String genService;
    /**
     * 需要生成Service实现类的模块名称（多个用逗号分割）
     */
    private String genServiceImpl;
    /**
     * 需要生成表实体类的模块名称（多个用逗号分割）
     */
    private String genEntity;
    /**
     * 需要生成Mapper接口的模块名称（多个用逗号分割）
     */
    private String genMapper;
    /**
     * 需要生成MapperXml的模块名称（多个用逗号分割）
     */
    private String genMapperXml;
    /**
     * 需要生成枚举类的模块名称（多个用逗号分割）
     */
    private String genEnum;
    /**
     * 转换数据
     */
    private ConvertData convertData;

    @Data
    public static class ConvertData {
        /**
         * 表名数组
         */
        private final List<String> tableList = new ArrayList<>(8);
        /**
         * 模块名数组
         */
        private final List<String> moduleList = new ArrayList<>(8);
        /**
         * 过滤表名前缀数组
         */
        private final List<String> filterTableNamePrefixList = new ArrayList<>(8);
        /**
         * 过滤列名前缀数组
         */
        private final List<String> filterColumnNamePrefixList = new ArrayList<>(8);
        /**
         * k - 服务名枚举
         * v - 服务生成配置集合
         */
        private final ConcurrentMap<ServiceNameEnum, List<ServiceGenDefinition>> serviceGenDefinitionMap = new ConcurrentHashMap<>(16);
    }

    /**
     * 初始化当前对象属性
     *
     * @param properties 配置参数对象
     */
    public void init(Properties properties) throws Exception {
        this.projectPath = properties.getProperty("projectPath", "");
        this.modules = properties.getProperty("modules", "");
        this.author = properties.getProperty("author", "");
        this.tables = properties.getProperty("tables", "");
        this.filterTableNamePrefix = properties.getProperty("filterTableNamePrefix", "");
        this.filterColumnNamePrefix = properties.getProperty("filterColumnNamePrefix", "");
        this.overwriteFile = Boolean.parseBoolean(properties.getProperty("overwriteFile", "false"));
        this.databaseDriverClassName = properties.getProperty("databaseDriverClassName", "");
        this.databaseUrl = properties.getProperty("databaseUrl", "");
        this.databaseUsername = properties.getProperty("databaseUsername", "");
        this.databasePassword = properties.getProperty("databasePassword", "");
        this.genController = properties.getProperty("genController", "");
        this.genService = properties.getProperty("genService", "");
        this.genServiceImpl = properties.getProperty("genServiceImpl", "");
        this.genEntity = properties.getProperty("genEntity", "");
        this.genMapper = properties.getProperty("genMapper", "");
        this.genMapperXml = properties.getProperty("genMapperXml", "");
        this.genEnum = properties.getProperty("genEnum", "");

        this.initConvertData();
    }


    /**
     * 初始化转换器对象
     */
    private void initConvertData() throws Exception {
        ConvertData initConvertData = new ConvertData();
        initConvertData.tableList.addAll(convertList(this.tables));
        initConvertData.moduleList.addAll(convertList(this.modules));
        initConvertData.filterTableNamePrefixList.addAll(convertList(this.filterTableNamePrefix));
        initConvertData.filterColumnNamePrefixList.addAll(convertList(this.filterColumnNamePrefix));
        for (String module : initConvertData.moduleList) {
            ServiceNameEnum serviceNameEnum = ServiceNameEnum.get(module);
            if (serviceNameEnum != null) {
                initConvertData.serviceGenDefinitionMap.put(serviceNameEnum, this.getServiceGenDefinitionList(module));
            }
        }
        initConvertData.serviceGenDefinitionMap.forEach(this::populateGenFilePath);

        this.convertData = initConvertData;
    }

    private List<ServiceGenDefinition> getServiceGenDefinitionList(String module) {
        List<ServiceGenDefinition> list = new ArrayList<>(16);
        if (this.genController.contains(module)) {
            ServiceGenDefinition serviceGenDefinition = new ServiceGenDefinition();
            serviceGenDefinition.setTypeName(TemplateFileNameEnum.Controller.name());
            serviceGenDefinition.setIsGen(true);
            list.add(serviceGenDefinition);
        }
        if (this.genService.contains(module)) {
            ServiceGenDefinition serviceGenDefinition = new ServiceGenDefinition();
            serviceGenDefinition.setTypeName(TemplateFileNameEnum.Service.name());
            serviceGenDefinition.setIsGen(true);
            list.add(serviceGenDefinition);
        }
        if (this.genServiceImpl.contains(module)) {
            ServiceGenDefinition serviceGenDefinition = new ServiceGenDefinition();
            serviceGenDefinition.setTypeName(TemplateFileNameEnum.ServiceImpl.name());
            serviceGenDefinition.setIsGen(true);
            list.add(serviceGenDefinition);
        }
        if (this.genEntity.contains(module)) {
            ServiceGenDefinition serviceGenDefinition = new ServiceGenDefinition();
            serviceGenDefinition.setTypeName(TemplateFileNameEnum.Entity.name());
            serviceGenDefinition.setIsGen(true);
            list.add(serviceGenDefinition);
        }
        if (this.genMapper.contains(module)) {
            ServiceGenDefinition serviceGenDefinition = new ServiceGenDefinition();
            serviceGenDefinition.setTypeName(TemplateFileNameEnum.Mapper.name());
            serviceGenDefinition.setIsGen(true);
            list.add(serviceGenDefinition);
        }
        if (this.genMapperXml.contains(module)) {
            ServiceGenDefinition serviceGenDefinition = new ServiceGenDefinition();
            serviceGenDefinition.setTypeName(TemplateFileNameEnum.MapperXml.name());
            serviceGenDefinition.setIsGen(true);
            list.add(serviceGenDefinition);
        }
        if (this.genEnum.contains(module)) {
            ServiceGenDefinition serviceGenDefinition = new ServiceGenDefinition();
            serviceGenDefinition.setTypeName(TemplateFileNameEnum.Enum.name());
            serviceGenDefinition.setIsGen(true);
            list.add(serviceGenDefinition);
        }
        return list;
    }

    /**
     * 填充生成的类的文件路径
     */
    @SuppressWarnings("all")
    private void populateGenFilePath(ServiceNameEnum serviceNameEnum, List<ServiceGenDefinition> serviceGenDefinitionList) {
        for (ServiceGenDefinition serviceGenDefinition : serviceGenDefinitionList) {
            if (Constant.Api.SERVICE_NAME == serviceNameEnum) {
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Controller.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Api.BASE_PACKAGE + StrPool.DOT + Constant.CONTROLLER_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_CONTROLLER_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Service.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Api.BASE_PACKAGE + StrPool.DOT + Constant.SERVICE_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_SERVICE_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.ServiceImpl.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Api.BASE_PACKAGE + StrPool.DOT + Constant.SERVICE_IMPL_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_SERVICE_IMPL_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Entity.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Api.BASE_PACKAGE + StrPool.DOT + Constant.ENTITY_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_ENTITY_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Mapper.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Api.BASE_PACKAGE + StrPool.DOT + Constant.MAPPER_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_MAPPER_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.MapperXml.name())) {
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_MAPPER_XML_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Enum.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Api.BASE_PACKAGE + StrPool.DOT + Constant.ENUM_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_ENUM_PACKAGE);
                    continue;
                }
            } else if (Constant.Admin.SERVICE_NAME == serviceNameEnum) {
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Controller.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Admin.BASE_PACKAGE + StrPool.DOT + Constant.CONTROLLER_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_CONTROLLER_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Service.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Admin.BASE_PACKAGE + StrPool.DOT + Constant.SERVICE_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_SERVICE_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.ServiceImpl.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Admin.BASE_PACKAGE + StrPool.DOT + Constant.SERVICE_IMPL_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_SERVICE_IMPL_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Entity.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Admin.BASE_PACKAGE + StrPool.DOT + Constant.ENTITY_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_ENTITY_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Mapper.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Admin.BASE_PACKAGE + StrPool.DOT + Constant.MAPPER_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_MAPPER_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.MapperXml.name())) {
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_MAPPER_XML_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Enum.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Admin.BASE_PACKAGE + StrPool.DOT + Constant.ENUM_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_ENUM_PACKAGE);
                    continue;
                }
            } else if (Constant.Dao.SERVICE_NAME == serviceNameEnum) {
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Controller.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Dao.BASE_PACKAGE + StrPool.DOT + Constant.CONTROLLER_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_CONTROLLER_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Service.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Dao.BASE_PACKAGE + StrPool.DOT + Constant.SERVICE_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_SERVICE_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.ServiceImpl.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Dao.BASE_PACKAGE + StrPool.DOT + Constant.SERVICE_IMPL_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_SERVICE_IMPL_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Entity.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Dao.BASE_PACKAGE + StrPool.DOT + Constant.ENTITY_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_ENTITY_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Mapper.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Dao.BASE_PACKAGE + StrPool.DOT + Constant.MAPPER_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_MAPPER_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.MapperXml.name())) {
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_MAPPER_XML_PACKAGE);
                    continue;
                }
                if (serviceGenDefinition.getIsGen()
                        && serviceGenDefinition.getTypeName().equalsIgnoreCase(TemplateFileNameEnum.Enum.name())) {
                    serviceGenDefinition.setPackagePath(Constant.Dao.BASE_PACKAGE + StrPool.DOT + Constant.ENUM_PACKAGE);
                    serviceGenDefinition.setLocalPath(this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_ENUM_PACKAGE);
                    continue;
                }
            } else {
                System.out.println("类型未找到");
            }
        }
    }

    /**
     * 根据逗号作为分割符，将字符串拆分成List集合
     *
     * @param param 带逗号的字符串
     * @return 拆分后的List集合
     * @throws Exception 拆分后List集合为空抛出异常
     */
    private static List<String> convertList(String param) throws Exception {
        String[] tableArr = param.split(StrPool.COMMA);
        if (ArrayUtil.isEmpty(tableArr)) {
            throw new Exception("生成器配置参数值【" + param + "】格式错误, 多个请用逗号分割");
        }
        List<String> list = Arrays.asList(tableArr);
        list.forEach(table -> table = table.trim());
        return list;
    }
}
