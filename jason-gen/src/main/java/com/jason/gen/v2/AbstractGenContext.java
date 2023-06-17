package com.jason.gen.v2;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 上下文
 *
 * @author guozhongcheng
 * @since 2023/6/13
 **/
public abstract class AbstractGenContext {
    protected static final String GEN_ARGS_PROPERTIES_FILE_NAME = "genPhotography.properties";
    protected static final String TYPE_CONVERT_PROPERTIES_FILE_NAME = "type-converter.properties";

    protected AbstractGenContext(AbstractDatabase abstractDatabase, TemplateEngine templateEngine) {
        this.abstractDatabase = abstractDatabase;
        this.templateEngine = templateEngine;
    }

    /**
     * 生成器配置参数
     */
    protected Properties genArgsProperties = new Properties(16);
    /**
     * 数据库列类型映射
     */
    protected Properties typeConvertProperties = new Properties(16);
    /**
     * 生成器配置参数对象
     */
    protected GenArgs genArgs = new GenArgs();
    /**
     * Mysql数据库对象
     */
    protected final AbstractDatabase abstractDatabase;
    /**
     * 模板引擎
     */
    protected final TemplateEngine templateEngine;

    /**
     * k - 服务名枚举
     * v - 服务生成配置
     */
    private final ConcurrentMap<ServiceNameEnum, ServiceGenConfig> serviceGenConfigMap = new ConcurrentHashMap<>(16);
    /**
     * K - 保存表名
     * V - 保存了表名、表注释和列相关信息
     */
    protected final ConcurrentMap<String, TableDefinition> tableDefinitionMap = new ConcurrentHashMap<>(16);
    /**
     * K - 数据库字段类型名
     * V - Java类型定义
     */
    protected final ConcurrentMap<String, TypeConvertDefinition> typeConvertMap = new ConcurrentHashMap<>(32);
    /**
     * 模板定义信息集合
     */
    protected final ConcurrentMap<TemplateFileNameEnum, TemplateDefinition> templateDefinitionMap = new ConcurrentHashMap<>(16);
    /**
     *
     */
    protected final ConcurrentMap<ServiceNameEnum, List<OutputFileDefinition>> outputFileDefinitionMap = new ConcurrentHashMap<>(32);

    /**
     * 构建
     */
    public void build() throws Exception {
        // 准备构建
        prepareBuild();

        // 加载配置文件
        loadConfiguration();

        // 解析配置文件
        parseConfiguration();

        // 加载数据库表信息
        loadDataBaseTableInfo(genArgs);

        // 过滤表名前缀
        filterTableNamePrefix(this.genArgs.getConvertData().getFilterTableNamePrefixList(), this.tableDefinitionMap);

        // 过滤列名前缀
        filterColumnNamePrefix(this.genArgs.getConvertData().getFilterColumnNamePrefixList(), this.tableDefinitionMap);

        // 填充数据库列类型映射Java类型
        populateColumnTypeConverter();

        // 填充表名和字段名映射java类名和属性名
        populateTableNameAndColumnNameConverter();

        // 加载模板定义信息
        loadTemplates(genArgs);

        // 填充列类型映射java枚举类型
        populateColumnEnumConverter();

        // 构建输出文件定义信息
        buildOutputFileDefinition();

        // 填充模板数据
        populateTemplatePopulateData();

        // 代码生成前最好一次调整机会
        lastAdjustment();

        // 代码生成前最后一次校验
        lastCheck();

        // 执行代码生成
        executeCodeGeneration();
    }

    protected void filterTableNamePrefix(List<String> filterTableNamePrefixList, ConcurrentMap<String, TableDefinition> tableDefinitionMap) {
        if (CollUtil.isNotEmpty(filterTableNamePrefixList)) {
            for (String filterTableNamePrefix : filterTableNamePrefixList) {
                tableDefinitionMap.values().forEach(item -> {
                    String tableName = item.getTableName();
                    if (tableName.indexOf(filterTableNamePrefix) == 0) {
                        String substring = tableName.substring(filterTableNamePrefix.length());
                        item.setFilterTableName(substring);
                    }
                });
            }
        }
    }

    protected void filterColumnNamePrefix(List<String> columnTableNamePrefixList, ConcurrentMap<String, TableDefinition> tableDefinitionMap) {
        if (CollUtil.isNotEmpty(columnTableNamePrefixList)) {
            for (String columnTableNamePrefix : columnTableNamePrefixList) {
                tableDefinitionMap.values().forEach(item -> {
                    List<ColumnDefinition> columnDefinitionsList = item.getColumnDefinitionList();
                    columnDefinitionsList.forEach(n -> {
                        String columnName = n.getColumnName();
                        if (columnName.indexOf(columnTableNamePrefix) == 0) {
                            String substring = columnName.substring(columnTableNamePrefix.length());
                            n.setFilterColumnName(substring);
                        }
                    });
                });
            }
        }
    }

    /**
     * 核心
     */
    protected void buildOutputFileDefinition() throws Exception {
        for (ServiceNameEnum serviceNameEnum : this.serviceGenConfigMap.keySet()) {
            List<OutputFileDefinition> outputFileDefinitionList = new ArrayList<>(16);
            ServiceGenConfig serviceGenConfig = this.serviceGenConfigMap.get(serviceNameEnum);
            for (Field field : serviceGenConfig.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object valueObj = field.get(serviceGenConfig);
                boolean boo = valueObj instanceof Boolean;
                if (!boo) {
                    continue;
                }
                if ((boolean) valueObj) {
                    for (TableDefinition tableDefinition : this.tableDefinitionMap.values()) {
                        // 构建输出文件定义信息
                        this.buildOutputFileDefinition(fieldName, serviceNameEnum, outputFileDefinitionList, serviceGenConfig, tableDefinition);
                    }
                }
            }
            this.buildOutputEnumFileDefinition(outputFileDefinitionList,
                    serviceGenConfig,
                    this.tableDefinitionMap.values().stream().findFirst().get());
            this.outputFileDefinitionMap.put(serviceNameEnum, outputFileDefinitionList);
        }
    }

    private void buildOutputEnumFileDefinition(List<OutputFileDefinition> outputFileDefinitionList,
                                               ServiceGenConfig serviceGenConfig,
                                               TableDefinition tableDefinition) {
        for (ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitionList()) {
            if (columnDefinition.getEnumType() && serviceGenConfig.getGenEnum()) {
                String outputEnumFileName = CharSequenceUtil.upperFirst(columnDefinition.getJavaFieldName()) + "Enum";
                for (OutputFileDefinition fileDefinition : outputFileDefinitionList) {
                    if (fileDefinition.getOutputFileName().equals(outputEnumFileName)) {
                        return;
                    }
                }
                OutputFileDefinition outputEnumFileDefinition = new OutputFileDefinition();
                TemplateDefinition templateDefinition = this.templateDefinitionMap.get(TemplateFileNameEnum.EnumP);
                if (templateDefinition != null) {
                    outputEnumFileDefinition.setTemplateDefinition(templateDefinition);
                }
                outputEnumFileDefinition.setOutputFileName(outputEnumFileName);
                outputEnumFileDefinition.setFullOutputFileName(outputEnumFileName + StrPool.DOT + "java");
                String baseOutputEnumFilePath = (String) ReflectUtil.getFieldValue(serviceGenConfig, "genEnumPath");
                outputEnumFileDefinition.setBaseOutputFilePath(baseOutputEnumFilePath);
                TemplatePopulateData populateData = new TemplatePopulateData();
                populateData.setPackageEnum(serviceGenConfig.getPackageEnum());
                populateData.setEnumClassName(outputEnumFileName);
                populateData.setEnumColumnDefinition(columnDefinition);
                outputEnumFileDefinition.setPopulateData(populateData);
                outputFileDefinitionList.add(outputEnumFileDefinition);
                System.out.println("");
            }
        }
    }

    /**
     * 构建输出文件定义信息
     *
     * @param fieldName                列名
     * @param serviceNameEnum          服务名称枚举
     * @param outputFileDefinitionList 输出文件定义信息集合
     * @param serviceGenConfig         服务生成配置
     * @param tableDefinition          表定义信息
     */
    private void buildOutputFileDefinition(String fieldName,
                                           ServiceNameEnum serviceNameEnum,
                                           List<OutputFileDefinition> outputFileDefinitionList,
                                           ServiceGenConfig serviceGenConfig,
                                           TableDefinition tableDefinition) {
        OutputFileDefinition outputFileDefinition = new OutputFileDefinition();
        String outputFileName = convertFileNameSuffix(tableDefinition.getJavaClassName(), fieldName);
        outputFileDefinition.setOutputFileName(outputFileName);
        outputFileDefinition.setFullOutputFileName(outputFileName + StrPool.DOT + (fieldName.contains("Xml") ? "xml" : "java"));
        String baseOutputFilePath = (String) ReflectUtil.getFieldValue(serviceGenConfig, fieldName + "Path");
        outputFileDefinition.setBaseOutputFilePath(baseOutputFilePath);
        String templateFileName = fieldName.replace("gen", "") + "P";
        TemplateDefinition templateDefinition = this.templateDefinitionMap.get(TemplateFileNameEnum.get(templateFileName));
        if (templateDefinition != null) {
            outputFileDefinition.setTemplateDefinition(templateDefinition);
        }
        // 获取java文件后缀
        String javaClassNameSuffix = TemplateFileNameEnum.getJavaClassNameSuffix(TemplateFileNameEnum.get(templateFileName));

        TemplatePopulateData populateData = BeanUtil.copyProperties(tableDefinition, TemplatePopulateData.class);
        String tempTableName = StrUtil.isNotBlank(tableDefinition.getFilterTableName())
                ? tableDefinition.getFilterTableName()
                : tableDefinition.getTableName();
        populateData.setApiBasePath(serviceNameEnum.name().toLowerCase() + StrPool.SLASH + tempTableName.replace(StrPool.UNDERLINE, StrPool.SLASH));
        populateData.setModuleName(CharSequenceUtil.lowerFirst(serviceNameEnum.name().toLowerCase()));
        populateData.setTableName(tableDefinition.getTableName());
        populateData.setAuthor(this.genArgs.getAuthor());
        populateData.setDate(DateUtil.today());
        String populateDataFieldName = "package" + fieldName.replace("gen", "");
        String baseOutputFilePackage = (String) ReflectUtil.getFieldValue(serviceGenConfig, populateDataFieldName);
        ReflectUtil.setFieldValue(populateData, populateDataFieldName, baseOutputFilePackage);
        String populateDataFieldName1 = CharSequenceUtil.lowerFirst(fieldName.replace("gen", "")) + "ClassName";
        String fullOutputFileName = tableDefinition.getJavaClassName() + javaClassNameSuffix;
        ReflectUtil.setFieldValue(populateData, populateDataFieldName1, fullOutputFileName);
        outputFileDefinition.setPopulateData(populateData);
        if (!outputFileName.equalsIgnoreCase(tableDefinition.getJavaClassName() + "Enum")) {
            outputFileDefinitionList.add(outputFileDefinition);

        }
    }

    private String convertFileNameSuffix(String tableJavaClassName, String fieldName) {
        String fileNameSuffix = switch (fieldName) {
            case "genController" -> "Controller";
            case "genService" -> "Service";
            case "genServiceImpl" -> "ServiceImpl";
            case "genEntity" -> "";
            case "genMapper", "genMapperXml" -> "Mapper";
            case "genEnum" -> "Enum";
            default -> "";
        };
        return tableJavaClassName + fileNameSuffix;
    }

    private void lastAdjustment() {

    }

    protected void executeCodeGeneration() throws Exception {
        templateEngine.outputFile(this.genArgs, this.outputFileDefinitionMap);
    }

    protected void lastCheck() {
//        CollUtil.isEmpty(this.templateDefinitionList);
    }

    protected void populateTemplatePopulateData() throws Exception {
        this.templateEngine.populateTemplateDefinition(
                this.templateDefinitionMap,
                this.tableDefinitionMap,
                this.typeConvertMap,
                this.outputFileDefinitionMap);
    }

    protected void loadTemplates(GenArgs genArgs) throws Exception {
        this.templateDefinitionMap.putAll(templateEngine.loadTemplates(genArgs));
        if (CollUtil.isEmpty(this.templateDefinitionMap)) {
            throw new Exception("未加载模板文件");
        }
    }

    protected void prepareBuild() {
        // 嘿嘿嘿
    }

    protected void loadConfiguration() throws Exception {
        this.genArgsProperties = loadProperties(GEN_ARGS_PROPERTIES_FILE_NAME);
        this.typeConvertProperties = loadProperties(TYPE_CONVERT_PROPERTIES_FILE_NAME);
        if (this.genArgsProperties.isEmpty()) {
            throw new Exception("加载" + GEN_ARGS_PROPERTIES_FILE_NAME + "文件失败");
        }
        if (this.typeConvertProperties.isEmpty()) {
            throw new Exception("加载" + TYPE_CONVERT_PROPERTIES_FILE_NAME + "文件失败");
        }
    }

    protected void parseConfiguration() throws Exception {
        // 解析生成器配置
        Map<Object, Object> genArgsMap = CollUtil.toMap(this.genArgsProperties.entrySet());
        if (CollUtil.isEmpty(genArgsMap)) {
            throw new Exception("配置参数转换Map失败");
        }
        this.genArgs = BeanUtil.toBean(genArgsMap, GenArgs.class);
        if (this.genArgs == null) {
            throw new Exception("配置参数转换Bean失败");
        }
        // 初始化
        this.genArgs.init(this.genArgsProperties);

        // 解析类型映射参数
        final Map<Object, Object> typeConvertHashMap = CollUtil.toMap(this.typeConvertProperties.entrySet());
        if (CollUtil.isEmpty(typeConvertHashMap)) {
            throw new Exception("类型映射参数转换Map失败");
        }
        for (Object k : typeConvertHashMap.keySet()) {
            // 放入集合元素
            this.putTypeConvertMap(k, typeConvertHashMap.get(k));
        }
        // 赋值服务生成配置集合
        this.serviceGenConfigMap.putAll(this.genArgs.getConvertData().getServiceGenConfigMap());
    }

    protected void loadDataBaseTableInfo(GenArgs genArgs) throws Exception {
        ConcurrentMap<String, TableDefinition> tableDefinitionConcurrentMap = abstractDatabase.init(genArgs);
        this.tableDefinitionMap.putAll(tableDefinitionConcurrentMap);
    }

    protected void populateColumnTypeConverter() {
        for (String tableName : this.tableDefinitionMap.keySet()) {
            TableDefinition tableDefinition = this.tableDefinitionMap.get(tableName);
            // 将_转换成大驼峰
            tableDefinition.setJavaClassName(NamingCase.toPascalCase(
                    StrUtil.isNotBlank(tableDefinition.getFilterTableName()) ?
                            tableDefinition.getFilterTableName() :
                            tableDefinition.getTableName()));
            for (ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitionList()) {
                columnDefinition.setJavaFieldName(NamingCase.toPascalCase(
                        StrUtil.isNotBlank(columnDefinition.getFilterColumnName()) ?
                                columnDefinition.getFilterColumnName() :
                                columnDefinition.getColumnName()));
                this.typeConvertMap.forEach((k, v) -> {
                    String jdbcTypeName = columnDefinition.getJdbcTypeName();
                    if (jdbcTypeName.equalsIgnoreCase(k)) {
                        columnDefinition.setJavaTypeName(v.getJavaTypeName());
                        columnDefinition.setJavaTypePackage(v.getJavaTypePackage());
                    }
                });
            }
        }
    }

    private void populateTableNameAndColumnNameConverter() {
        for (String tableName : this.tableDefinitionMap.keySet()) {
            TableDefinition tableDefinition = this.tableDefinitionMap.get(tableName);
            // 将_转换成大驼峰
            tableDefinition.setJavaClassName(NamingCase.toPascalCase(
                    StrUtil.isNotBlank(tableDefinition.getFilterTableName()) ?
                            tableDefinition.getFilterTableName() :
                            tableDefinition.getTableName()));
            for (ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitionList()) {
                columnDefinition.setJavaFieldName(NamingCase.toCamelCase(
                        StrUtil.isNotBlank(columnDefinition.getFilterColumnName()) ?
                                columnDefinition.getFilterColumnName() :
                                columnDefinition.getColumnName()));
            }
        }
    }

    protected void populateColumnEnumConverter() throws Exception {
        // 遍历
        for (String tableName : this.tableDefinitionMap.keySet()) {
            TableDefinition tableDefinition = this.tableDefinitionMap.get(tableName);
            // 校验参数
            JpValidationUtil.check(tableDefinition);
            // 遍历
            for (ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitionList()) {
                String columnComment = columnDefinition.getColumnComment();
                List<EnumDefinition> enumDefinitionList = buildEnumDefinitionList(columnComment);
                columnDefinition.setEnumType(false);
                if (CollUtil.isNotEmpty(enumDefinitionList)) {
                    String javaEnumTypeName = NamingCase.toPascalCase(columnDefinition.getJavaFieldName()) + "Enum";
                    columnDefinition.setJavaTypeName(javaEnumTypeName);
                    if (NumberUtil.isNumber(columnDefinition.getDefaultValue())) {
                        // 填充枚举信息
                        enumDefinitionList.stream()
                                .filter(n -> n.getValue().equals(Integer.valueOf(columnDefinition.getDefaultValue())))
                                .findFirst()
                                .ifPresent(n -> columnDefinition.setDefaultValue(javaEnumTypeName + StrPool.DOT + n.getEnumName()));
                    }
                    columnDefinition.setJavaTypePackage(Constant.Dao.BASE_PACKAGE + StrPool.DOT + Constant.ENUM_PACKAGE);
                    columnDefinition.setEnumType(true);
                    columnDefinition.setColumnCommentBrief(CharSequenceUtil.subBefore(columnComment, "（", false));
                    columnDefinition.setEnumDefinitionList(enumDefinitionList);
                }
            }
        }
    }

    private static List<EnumDefinition> buildEnumDefinitionList(String columnComment) {
        List<EnumDefinition> enumDefinitionList = new ArrayList<>(16);
        if (isEnumField(columnComment)) {
            int indexBegin = columnComment.indexOf("（");
            int indexEnd = columnComment.indexOf("）");
            String enumValueStr = columnComment.substring(indexBegin + 1, indexEnd).trim();
            String[] split1 = enumValueStr.split("，");
            for (String s : split1) {
                EnumDefinition enumDefinition = new EnumDefinition();
                String[] split2 = s.split("、");
                Integer value = Integer.valueOf(split2[0].trim());
                enumDefinition.setValue(value);
                String desc = split2[1].trim();
                enumDefinition.setDesc(desc);
                enumDefinition.setEnumName(PinyinUtil.getFirstLetter(desc, "") + value);
                enumDefinitionList.add(enumDefinition);
            }
        }
        return enumDefinitionList;
    }

    private static boolean isEnumField(String remarks) {
        if (remarks.contains("状态") || remarks.contains("类型") || remarks.contains("删除标志")) {
            return remarks.contains("（") && remarks.contains("）");
        }
        return false;
    }

    /**
     * 加载配置
     *
     * @param propertiesFileName 配置文件名
     * @return Properties 配置
     */
    private static Properties loadProperties(String propertiesFileName) throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = AbstractGenContext.class.getClassLoader().getResourceAsStream(propertiesFileName);
        try {
            properties.load(IoUtil.getReader(inputStream, Constant.CHARSET));
        } catch (IOException e) {
            throw new Exception("加载properties文件发生异常", e);
        }
        return properties;
    }

    /**
     * 放入集合元素
     *
     * @param k 类型转换参数HashMap - Key值
     * @param v 类型转换参数HashMap - Value值
     */
    private void putTypeConvertMap(Object k, Object v) throws Exception {
        TypeConvertDefinition typeConvertDefinition = new TypeConvertDefinition();
        String key;
        if (k instanceof String) {
            key = String.valueOf(k);
        } else {
            throw new Exception("类型映射参数Key值不匹配");
        }
        String value;
        if (v instanceof String) {
            value = String.valueOf(v);
        } else {
            throw new Exception("类型映射参数Value值不匹配");
        }
        if (StrUtil.isAllNotBlank(key, value)) {
            int spiltIndex = value.lastIndexOf(StrPool.DOT);
            if (spiltIndex == -1) {
                throw new Exception("类型映射参数Value值格式错误，获取的格式->" + value + "，期望的格式->java.lang.String");
            }
            String javaTypeName = value.substring(spiltIndex + 1);
            if (StrUtil.isNotBlank(javaTypeName)) {
                typeConvertDefinition.setJdbcTypeName(key);
                typeConvertDefinition.setJavaTypeName(javaTypeName);
                typeConvertDefinition.setJavaTypePackage(value);
                typeConvertMap.put(key, typeConvertDefinition);
            }
        }
    }


}
