package com.jason.gen.service;

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
import com.jason.gen.constant.Constant;
import com.jason.gen.entity.*;
import com.jason.gen.enums.ServiceNameEnum;
import com.jason.gen.enums.TemplateFileNameEnum;
import com.jason.gen.util.GenCommonUtil;
import com.jason.gen.util.JpValidationUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 代码生成上下文
 * 主要使用模板方法设计模式，run()方法作为整个代码生成的业务逻辑流程入口
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
     * v - 服务生成配置集合
     */
    private final ConcurrentMap<ServiceNameEnum, List<ServiceGenDefinition>> serviceGenDefinitionMap = new ConcurrentHashMap<>(16);

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
     * 运行
     */
    public void run() throws Exception {
        // 1、准备运行
        prepareRun();

        // 2、加载配置文件
        System.out.println("正在加载配置文件中...");
        loadConfiguration();
        System.out.println("加载配置文件完成");

        // 3、解析配置文件
        System.out.println("正在解析配置文件中...");
        parseConfiguration();
        System.out.println("解析配置文件完成");

        // 4、加载数据库表信息
        System.out.println("正在加载数据库表信息...");
        loadDataBaseTableInfo(genArgs);
        System.out.println("数据库表信息加载完成");

        // 5、过滤表名前缀
        System.out.println("正在过滤表名前缀...");
        filterTableNamePrefix(this.genArgs.getConvertData().getFilterTableNamePrefixList(), this.tableDefinitionMap);
        System.out.println("过滤表名前缀完成");

        // 6、过滤列名前缀
        System.out.println("正在过滤列名前缀...");
        filterColumnNamePrefix(this.genArgs.getConvertData().getFilterColumnNamePrefixList(), this.tableDefinitionMap);
        System.out.println("过滤列名前缀完成");

        // 7、填充数据库列类型映射Java类型
        System.out.println("正在填充数据库列类型映射Java类型...");
        populateColumnTypeConverter();
        System.out.println("填充数据库列类型映射Java类型完成");

        // 8、填充表名和字段名映射java类名和属性名
        System.out.println("正在填充表名和字段名映射java类名和属性名...");
        populateTableNameAndColumnNameConverter();
        System.out.println("填充表名和字段名映射java类名和属性名完成");

        // 9、加载模板定义信息
        System.out.println("正在加载模板定义信息...");
        loadTemplates(genArgs);
        System.out.println("加载模板定义信息完成");

        // 10、填充列类型映射java枚举类型
        System.out.println("正在填充列类型映射java枚举类型...");
        populateColumnEnumConverter();
        System.out.println("填充列类型映射java枚举类型完成");

        // 11、构建输出文件定义信息
        System.out.println("正在构建输出文件定义信息...");
        buildOutputFileDefinition();
        System.out.println("构建输出文件定义信息完成");

        // 12、填充模板占位符数据
        System.out.println("正在填充模板占位符数据...");
        populateTemplatePlaceholderData();
        System.out.println("填充模板占位符数据完成");

        // 13、代码生成前最好一次调整机会
        lastAdjustment();

        // 14、代码生成前最后一次校验
        lastCheck();

        // 15、执行代码生成
        System.out.println("正在执行代码生成...");
        executeCodeGeneration();
        System.out.println("执行代码生成完成");
    }

    /**
     * 准备运行（前置扩展点）
     */
    protected void prepareRun() {
        // 如需使用，请重写
    }

    /**
     * 加载配置文件中的信息
     *
     * @throws Exception 异常
     */
    protected void loadConfiguration() throws Exception {
        // 加载生成器配置参数
        this.genArgsProperties = loadProperties(GEN_ARGS_PROPERTIES_FILE_NAME);
        // 加载类型转换参数
        this.typeConvertProperties = loadProperties(TYPE_CONVERT_PROPERTIES_FILE_NAME);
        if (this.genArgsProperties.isEmpty()) {
            throw new Exception("加载" + GEN_ARGS_PROPERTIES_FILE_NAME + "文件失败");
        }
        if (this.typeConvertProperties.isEmpty()) {
            throw new Exception("加载" + TYPE_CONVERT_PROPERTIES_FILE_NAME + "文件失败");
        }
    }

    /**
     * 解析配置参数信息
     *
     * @throws Exception 解析异常
     */
    protected void parseConfiguration() throws Exception {
        // 解析生成器配置信息
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
        this.serviceGenDefinitionMap.putAll(this.genArgs.getConvertData().getServiceGenDefinitionMap());
    }

    /**
     * 加载数据库表定义信息
     *
     * @param genArgs 生成器配置参数
     * @throws Exception 异常
     */
    protected void loadDataBaseTableInfo(GenArgs genArgs) throws Exception {
        ConcurrentMap<String, TableDefinition> tableDefinitionConcurrentMap = abstractDatabase.init(genArgs);
        this.tableDefinitionMap.putAll(tableDefinitionConcurrentMap);
    }

    /**
     * 过滤表名称前缀
     *
     * @param filterTableNamePrefixList 过滤表名称前缀字符信息集合
     * @param tableDefinitionMap        表定义信息集合
     */
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

    /**
     * 过滤列名称前缀
     *
     * @param columnTableNamePrefixList 过滤列名称前缀字符信息集合
     * @param tableDefinitionMap        表定义信息集合
     */
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
     * 填充列类型映射
     */
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
                // 数据库列类型映射Java类型
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

    /**
     * 对表名称和列名称进行转换，然后进行填充
     */
    protected void populateTableNameAndColumnNameConverter() {
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

    /**
     * 加载模板定义信息
     *
     * @param genArgs 生成器配置参数
     * @throws Exception 异常
     */
    protected void loadTemplates(GenArgs genArgs) throws Exception {
        this.templateDefinitionMap.putAll(templateEngine.loadTemplates(genArgs));
        if (CollUtil.isEmpty(this.templateDefinitionMap)) {
            throw new Exception("未加载模板文件");
        }
    }

    /**
     * 对列枚举进行转换，然后填充
     *
     * @throws Exception 异常
     */
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

    /**
     * 构建输出文件定义信息（核心）
     */
    protected void buildOutputFileDefinition() throws Exception {
        for (ServiceNameEnum serviceNameEnum : this.serviceGenDefinitionMap.keySet()) {
            List<OutputFileDefinition> outputFileDefinitionList = new ArrayList<>(16);
            List<ServiceGenDefinition> serviceGenDefinitionList = this.serviceGenDefinitionMap.get(serviceNameEnum);
            for (ServiceGenDefinition serviceGenDefinition : serviceGenDefinitionList) {
                for (TableDefinition tableDefinition : this.tableDefinitionMap.values()) {
                    // 构建输出文件定义信息
                    this.buildOutputFileDefinitionList(serviceGenDefinition, serviceNameEnum, tableDefinition, outputFileDefinitionList);
                }
            }
            // 获取枚举类型生成配置定义信息
            ServiceGenDefinition enumServiceGenDefinition = serviceGenDefinitionList.stream().filter(n -> n.getTypeName().equals(TemplateFileNameEnum.Enum.name()))
                    .findFirst().orElse(new ServiceGenDefinition());
            // 构建输出枚举文件定义信息
            this.buildOutputEnumFileDefinition(serviceNameEnum, enumServiceGenDefinition, this.tableDefinitionMap.values(), outputFileDefinitionList);
            // 放入集合
            this.outputFileDefinitionMap.put(serviceNameEnum, outputFileDefinitionList);
        }
    }

    /**
     * 填充模板占位符数据
     *
     * @throws Exception 异常
     */
    protected void populateTemplatePlaceholderData() throws Exception {
        this.templateEngine.populateTemplateDefinition(
                this.templateDefinitionMap,
                this.tableDefinitionMap,
                this.typeConvertMap,
                this.outputFileDefinitionMap);
    }

    /**
     * 代码生成前最好一次调整机会
     */
    protected void lastAdjustment() {
        // 如需使用，请重写
    }

    /**
     * 代码生成前最后一次校验
     */
    protected void lastCheck() {
        // 如需使用，请重写
    }

    /**
     * 执行代码生成
     *
     * @throws Exception 异常
     */
    protected void executeCodeGeneration() throws Exception {
        templateEngine.outputFile(this.genArgs, this.outputFileDefinitionMap);
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

    /**
     * 构建输出文件定义信息
     *
     * @param serviceGenDefinition     服务生成定义信息
     * @param serviceNameEnum          服务名称枚举
     * @param tableDefinition          表定义信息
     * @param outputFileDefinitionList 输出文件定义信息集合
     * @throws Exception 异常
     */
    private void buildOutputFileDefinitionList(final ServiceGenDefinition serviceGenDefinition,
                                               final ServiceNameEnum serviceNameEnum,
                                               final TableDefinition tableDefinition,
                                               final List<OutputFileDefinition> outputFileDefinitionList) throws Exception {
        OutputFileDefinition outputFileDefinition = new OutputFileDefinition();
        String templateFileName = serviceGenDefinition.getTypeName();
        TemplateFileNameEnum templateFileNameEnum = TemplateFileNameEnum.get(templateFileName);
        TemplateDefinition templateDefinition = this.templateDefinitionMap.get(templateFileNameEnum);
        if (templateDefinition == null) {
            throw new Exception("【" + templateFileName + "】获取模板定义信息为空");
        }
        outputFileDefinition.setTemplateDefinition(templateDefinition);
        // 文件名称
        String outputFileName = tableDefinition.getJavaClassName() + templateFileNameEnum.getJavaClassNameSuffix();
        // 过滤枚举类型，方便后面统一处理枚举类型
        if (outputFileName.equalsIgnoreCase(tableDefinition.getJavaClassName() + TemplateFileNameEnum.Enum.getJavaClassNameSuffix())) {
            return;
        }
        outputFileDefinition.setOutputFileName(outputFileName);
        outputFileDefinition.setFullOutputFileName(outputFileName + StrPool.DOT + (templateFileName.contains("Xml") ? "xml" : "java"));
        // 文件输出路径
        outputFileDefinition.setBaseOutputFilePath(serviceGenDefinition.getLocalPath());
        // 模板占位符数据
        TemplatePlaceholderData placeholderData = BeanUtil.copyProperties(tableDefinition, TemplatePlaceholderData.class);
        String tempTableName = StrUtil.isNotBlank(tableDefinition.getFilterTableName())
                ? tableDefinition.getFilterTableName()
                : tableDefinition.getTableName();
        placeholderData.setApiBasePath(serviceNameEnum.name().toLowerCase() + StrPool.SLASH + tempTableName.replace(StrPool.UNDERLINE, StrPool.SLASH));
        placeholderData.setModuleName(CharSequenceUtil.lowerFirst(serviceNameEnum.name().toLowerCase()));
        placeholderData.setTableName(tableDefinition.getTableName());
        placeholderData.setAuthor(this.genArgs.getAuthor());
        placeholderData.setDate(DateUtil.today());
        // 赋值包路径
        ReflectUtil.setFieldValue(placeholderData, "package" + templateFileName, serviceGenDefinition.getPackagePath());
        String populateDataFieldName1 = CharSequenceUtil.lowerFirst(templateFileName) + "ClassName";
        String fullOutputFileName = tableDefinition.getJavaClassName() + templateFileNameEnum.getJavaClassNameSuffix();
        ReflectUtil.setFieldValue(placeholderData, populateDataFieldName1, fullOutputFileName);
        outputFileDefinition.setPopulateData(placeholderData);
        outputFileDefinitionList.add(outputFileDefinition);
    }

    /**
     * 构建输出枚举文件定义信息
     *
     * @param serviceNameEnum           服务名称枚举
     * @param serviceGenDefinition      服务生成定义信息
     * @param tableDefinitionCollection 表定义信息集合
     * @param outputFileDefinitionList  输出文件定义信息集合
     */
    private void buildOutputEnumFileDefinition(ServiceNameEnum serviceNameEnum,
                                               ServiceGenDefinition serviceGenDefinition,
                                               Collection<TableDefinition> tableDefinitionCollection,
                                               List<OutputFileDefinition> outputFileDefinitionList) throws Exception {
        for (TableDefinition tableDefinition : tableDefinitionCollection) {
            for (ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitionList()) {
                if (columnDefinition.getEnumType() && serviceGenDefinition.getIsGen()) {
                    String outputEnumFileName = CharSequenceUtil.upperFirst(columnDefinition.getJavaFieldName()) + TemplateFileNameEnum.Enum.getJavaClassNameSuffix();
                    // 如果已存在则跳过
                    for (OutputFileDefinition fileDefinition : outputFileDefinitionList) {
                        if (fileDefinition.getOutputFileName().equals(outputEnumFileName)) {
                            return;
                        }
                    }
                    OutputFileDefinition outputEnumFileDefinition = new OutputFileDefinition();
                    TemplateDefinition templateDefinition = this.templateDefinitionMap.get(TemplateFileNameEnum.Enum);
                    if (templateDefinition == null) {
                        throw new Exception("【" + TemplateFileNameEnum.Enum + "】获取模板定义信息为空");
                    }
                    outputEnumFileDefinition.setTemplateDefinition(templateDefinition);
                    outputEnumFileDefinition.setOutputFileName(outputEnumFileName);
                    outputEnumFileDefinition.setFullOutputFileName(outputEnumFileName + StrPool.DOT + "java");
                    outputEnumFileDefinition.setBaseOutputFilePath(serviceGenDefinition.getLocalPath());
                    TemplatePlaceholderData populateData = new TemplatePlaceholderData();
                    populateData.setModuleName(CharSequenceUtil.lowerFirst(serviceNameEnum.name().toLowerCase()));
                    populateData.setPackageEnum(serviceGenDefinition.getPackagePath());
                    populateData.setEnumClassName(outputEnumFileName);
                    populateData.setEnumColumnDefinition(columnDefinition);
                    outputEnumFileDefinition.setPopulateData(populateData);
                    outputFileDefinitionList.add(outputEnumFileDefinition);
                }
            }
        }
    }

    /**
     * 构建枚举定义信息集合
     *
     * @param columnComment 枚举列注释
     * @return 枚举定义信息集合
     */
    private static List<EnumDefinition> buildEnumDefinitionList(String columnComment) {
        List<EnumDefinition> enumDefinitionList = new ArrayList<>(16);
        if (GenCommonUtil.isEnumField(columnComment)) {
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

}
