package com.jason.gen.v2;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
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
    protected final List<TemplateDefinition> templateDefinitionList = new ArrayList<>(16);

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

        // 填充模板定义参数
        populateTemplateDefinition();

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
                    List<ColumnDefinition> columnDefinitionsList = item.getColumnDefinitionsList();
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
    protected void buildOutputFileDefinition() {
        OutputFileDefinition definition = new OutputFileDefinition();
        // 获取需要生成代码的表名称
        GenArgs.ConvertData convertData = this.genArgs.getConvertData();
        for (String tableName : convertData.getTableList()) {
            TableDefinition tableDefinition = this.tableDefinitionMap.get(tableName);
            for (ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitionsList()) {
                TypeConvertDefinition typeConvertDefinition = this.typeConvertMap.get(columnDefinition.getColumnName());
                if (columnDefinition.getJdbcTypeName().equalsIgnoreCase(typeConvertDefinition.getJdbcTypeName())) {

                }
            }
        }

    }

    private void lastAdjustment() {

    }

    protected void executeCodeGeneration() throws Exception {
        templateEngine.outputFile(this.templateDefinitionList);
    }

    protected void lastCheck() {
        CollUtil.isEmpty(this.templateDefinitionList);
    }

    protected void populateTemplateDefinition() throws Exception {
        templateEngine.populateTemplateDefinition(
                this.templateDefinitionList,
                this.tableDefinitionMap,
                this.typeConvertMap);
    }

    protected void loadTemplates(GenArgs genArgs) throws Exception {
        this.templateDefinitionList.addAll(templateEngine.loadTemplates(genArgs));
        if (CollUtil.isEmpty(this.templateDefinitionList)) {
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
        genArgs.init(this.genArgsProperties);

        // 解析类型映射参数
        final Map<Object, Object> typeConvertHashMap = CollUtil.toMap(this.typeConvertProperties.entrySet());
        if (CollUtil.isEmpty(typeConvertHashMap)) {
            throw new Exception("类型映射参数转换Map失败");
        }
        for (Object k : typeConvertHashMap.keySet()) {
            // 放入集合元素
            this.putTypeConvertMap(k, typeConvertHashMap.get(k));
        }
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
            for (ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitionsList()) {
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
            for (ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitionsList()) {
                columnDefinition.setJavaFieldName(NamingCase.toPascalCase(
                        StrUtil.isNotBlank(columnDefinition.getFilterColumnName()) ?
                                columnDefinition.getFilterColumnName() :
                                columnDefinition.getColumnName()));
            }
        }
    }

    protected void populateColumnEnumConverter() throws Exception {
        for (String tableName : this.tableDefinitionMap.keySet()) {
            TableDefinition tableDefinition = this.tableDefinitionMap.get(tableName);
            // 校验参数
            JpValidationUtil.check(tableDefinition);
            for (ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitionsList()) {
                String columnComment = columnDefinition.getColumnComment();
                List<EnumDefinition> enumDefinitionList = buildEnumDefinitionList(columnComment);
                columnDefinition.setEnumType(false);
                if (CollUtil.isNotEmpty(enumDefinitionList)) {
                    for (EnumDefinition enumDefinition : enumDefinitionList) {
                        // 填充枚举信息
                        TemplateDefinition enumTemplateDefinition = this.templateDefinitionList.stream()
                                .filter(n -> TemplateFileNameEnum.EntityP.name().equalsIgnoreCase(n.getTemplateFileName())).findFirst()
                                .orElse(new TemplateDefinition());
                        columnDefinition.setJavaTypeName(NamingCase.toPascalCase(columnDefinition.getJavaFieldName()) + "Enum");
                        columnDefinition.setJavaTypePackage(enumTemplateDefinition.getBaseOutputFilePath());
                        // TODO
                        columnDefinition.setDefaultValue(null);
                        columnDefinition.setEnumType(true);
                    }
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
            for (int i = 0; i < split1.length; i++) {
                EnumDefinition enumDefinition = new EnumDefinition();
                String[] split2 = split1[i].split("、");
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
