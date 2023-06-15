package com.jason.gen.v2;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 生成器参数配置
 *
 * @author guozhongcheng
 * @since 2023/6/13
 **/
@Data
public class GenArgs implements Serializable {

    private String projectPath;
    private String modules;
    private String author;
    private String tables;
    private String databaseDriverClassName;
    private String databaseUrl;
    private String databaseUsername;
    private String databasePassword;
    private String genController;
    private String genService;
    private String genServiceImpl;
    private String genEntity;
    private String genMapper;
    private String genMapperXml;
    private String genEnum;
    private ConvertData convertData;

    @Data
    public static class ConvertData {
        /**
         * 表名数组
         */
        private final List<String> tableList = new ArrayList<>(16);
        /**
         * 模块名数组
         */
        private final List<String> moduleList = new ArrayList<>(16);
        /**
         * k - 服务名枚举
         * v - 服务生成配置
         */
        private final ConcurrentMap<ServiceNameEnum, ServiceGenConfig> serviceGenConfigMap = new ConcurrentHashMap<>(16);
    }

    /**
     * 二次初始化
     *
     * @param properties 配置参数对象
     */
    public void init(Properties properties) throws Exception {
        this.projectPath = properties.getProperty("projectPath");
        this.modules = properties.getProperty("modules");
        this.author = properties.getProperty("author");
        this.tables = properties.getProperty("tables");
        this.databaseDriverClassName = properties.getProperty("databaseDriverClassName");
        this.databaseUrl = properties.getProperty("databaseUrl");
        this.databaseUsername = properties.getProperty("databaseUsername");
        this.databasePassword = properties.getProperty("databasePassword");
        this.genController = properties.getProperty("genController");
        this.genService = properties.getProperty("genService");
        this.genServiceImpl = properties.getProperty("genServiceImpl");
        this.genEntity = properties.getProperty("genEntity");
        this.genMapper = properties.getProperty("genMapper");
        this.genMapperXml = properties.getProperty("genMapperXml");
        this.genEnum = properties.getProperty("genEnum");

        this.initConvertData();
    }


    /**
     * 初始化转换器
     */
    private void initConvertData() throws Exception {
        ConvertData initConvertData = new ConvertData();
        initConvertData.tableList.addAll(convertList(this.tables));
        initConvertData.moduleList.addAll(convertList(this.modules));
        for (String module : initConvertData.moduleList) {
            ServiceNameEnum serviceNameEnum = ServiceNameEnum.get(module);
            if (serviceNameEnum != null) {
                initConvertData.serviceGenConfigMap.put(serviceNameEnum, this.getServiceGenConfig(module));
            }
        }
        initConvertData.serviceGenConfigMap.forEach(this::populateGenFilePath);

        this.convertData = initConvertData;
    }

    /**
     * 填充生成的类的文件路径
     */
    private void populateGenFilePath(ServiceNameEnum serviceNameEnum, ServiceGenConfig serviceGenConfig) {
        if (Constant.Api.SERVICE_NAME == serviceNameEnum) {
            serviceGenConfig.setGenControllerPath(serviceGenConfig.getGenController() ? this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_CONTROLLER_PACKAGE : null);
            serviceGenConfig.setGenServicePath(serviceGenConfig.getGenService() ? this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_SERVICE_PACKAGE : null);
            serviceGenConfig.setGenServiceImplPath(serviceGenConfig.getGenServiceImpl() ? this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_SERVICE_IMPL_PACKAGE : null);
            serviceGenConfig.setGenEntityPath(serviceGenConfig.getGenEntity() ? this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_ENTITY_PACKAGE : null);
            serviceGenConfig.setGenMapperPath(serviceGenConfig.getGenMapper() ? this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_MAPPER_PACKAGE : null);
            serviceGenConfig.setGenMapperXmlPath(serviceGenConfig.getGenMapperXml() ? this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_MAPPER_XML_PACKAGE : null);
            serviceGenConfig.setGenEnumPath(serviceGenConfig.getGenEnum() ? this.projectPath + Constant.SEPARATOR + Constant.Api.FULL_ENUM_PACKAGE : null);
        } else if (Constant.Admin.SERVICE_NAME == serviceNameEnum) {
            serviceGenConfig.setGenControllerPath(serviceGenConfig.getGenController() ? this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_CONTROLLER_PACKAGE : null);
            serviceGenConfig.setGenServicePath(serviceGenConfig.getGenService() ? this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_SERVICE_PACKAGE : null);
            serviceGenConfig.setGenServiceImplPath(serviceGenConfig.getGenServiceImpl() ? this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_SERVICE_IMPL_PACKAGE : null);
            serviceGenConfig.setGenEntityPath(serviceGenConfig.getGenEntity() ? this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_ENTITY_PACKAGE : null);
            serviceGenConfig.setGenMapperPath(serviceGenConfig.getGenMapper() ? this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_MAPPER_PACKAGE : null);
            serviceGenConfig.setGenMapperXmlPath(serviceGenConfig.getGenMapperXml() ? this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_MAPPER_XML_PACKAGE : null);
            serviceGenConfig.setGenEnumPath(serviceGenConfig.getGenEnum() ? this.projectPath + Constant.SEPARATOR + Constant.Admin.FULL_ENUM_PACKAGE : null);
        } else if (Constant.Dao.SERVICE_NAME == serviceNameEnum) {
            serviceGenConfig.setGenControllerPath(serviceGenConfig.getGenController() ? this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_CONTROLLER_PACKAGE : null);
            serviceGenConfig.setGenServicePath(serviceGenConfig.getGenService() ? this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_SERVICE_PACKAGE : null);
            serviceGenConfig.setGenServiceImplPath(serviceGenConfig.getGenServiceImpl() ? this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_SERVICE_IMPL_PACKAGE : null);
            serviceGenConfig.setGenEntityPath(serviceGenConfig.getGenEntity() ? this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_ENTITY_PACKAGE : null);
            serviceGenConfig.setGenMapperPath(serviceGenConfig.getGenMapper() ? this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_MAPPER_PACKAGE : null);
            serviceGenConfig.setGenMapperXmlPath(serviceGenConfig.getGenMapperXml() ? this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_MAPPER_XML_PACKAGE : null);
            serviceGenConfig.setGenEnumPath(serviceGenConfig.getGenEnum() ? this.projectPath + Constant.SEPARATOR + Constant.Dao.FULL_ENUM_PACKAGE : null);
        } else {
            System.out.println("类型未找到");
        }
    }

    /**
     * 获取服务生成配置对象
     *
     * @param module 模块名
     * @return 服务生成配置对象
     */
    private ServiceGenConfig getServiceGenConfig(String module) {
        return ServiceGenConfig.builder()
                .genController(isContain(module, this.genController))
                .genService(isContain(module, this.genService))
                .genServiceImpl(isContain(module, this.genServiceImpl))
                .genEntity(isContain(module, this.genEntity))
                .genMapper(isContain(module, this.genMapper))
                .genMapperXml(isContain(module, this.genMapperXml))
                .genEnum(isContain(module, this.genEnum))
                .build();
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

    /**
     * 将params按照逗号拆分成数组，遍历数组全量匹配param
     *
     * @param param  需要匹配的字符串
     * @param params 匹配的字符串
     * @return 是否匹配上
     */
    private static boolean isContain(String param, String params) {
        if (StrUtil.isNotBlank(param) && StrUtil.isNotBlank(params)) {
            String[] split = params.split(StrPool.COMMA);
            if (ArrayUtil.isNotEmpty(split)) {
                String filterResult = Arrays.stream(split).filter(n -> n.equalsIgnoreCase(param)).findFirst().orElse(null);
                if (StrUtil.isNotBlank(filterResult)) {
                    return true;
                }
            }
        }
        return false;
    }
}
