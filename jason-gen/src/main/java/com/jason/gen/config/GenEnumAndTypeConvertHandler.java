package com.jason.gen.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.type.ITypeConvertHandler;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import com.jason.gen.GenPhotographyRun;
import com.jason.gen.constant.GenConstant;
import com.jason.gen.entity.ColumnInfo;
import com.jason.gen.entity.RemarksEnumInfo;
import com.jason.gen.util.GenCommonUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * 该类主要实现了两个功能:
 * 1.生成数据库表字段枚举类。
 * 2.对数据库表字段的JDBC类型与JAVA类型进行自定义映射
 *
 * @author guozhongcheng
 * @since 2023/6/9
 **/
@Slf4j
public class GenEnumAndTypeConvertHandler implements ITypeConvertHandler {

    private static final String SEPARATOR = GenConstant.SEPARATOR;

    private static final Properties PROPERTIES = new Properties();

    static {
        InputStream inputStream = GenEnumAndTypeConvertHandler.class
                .getClassLoader().getResourceAsStream("type-converter.properties");
        try {
            PROPERTIES.load(IoUtil.getReader(inputStream, Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @NotNull
    public IColumnType convert(GlobalConfig globalConfig, TypeRegistry typeRegistry, TableField.MetaInfo metaInfo) {
        IColumnType columnType = fieldTypeConvert(globalConfig, typeRegistry, metaInfo);

        if (GenConstant.DAO.equals(GenPhotographyRun.CURRENT_MODULES)) {
            ColumnInfo columnInfo = ColumnInfo.build(metaInfo);
            if (columnInfo != null) {
                RemarksEnumInfo remarksEnumInfo = RemarksEnumInfo.build(columnInfo, globalConfig);
                if (CollUtil.isNotEmpty(remarksEnumInfo.getEnumValueList())) {
                    createEnumFile(remarksEnumInfo);
                }
            }
        }
        return columnType;
    }

    private IColumnType fieldTypeConvert(GlobalConfig globalConfig, TypeRegistry typeRegistry, TableField.MetaInfo metaInfo) {
        String fieldType = metaInfo.getJdbcType().name();
        IColumnType columnType = null;
        String convertTypeName = null;
        // 第一次全字符匹配
        for (String item : PROPERTIES.stringPropertyNames()) {
            if (fieldType.toLowerCase().equalsIgnoreCase(item)) {
                convertTypeName = PROPERTIES.getProperty(item);
                break;
            }
        }
        //第一次全字符匹配未匹配上时，进行第二次模糊匹配
        if (StrUtil.isBlank(convertTypeName)) {
            for (String item : PROPERTIES.stringPropertyNames()) {
                if (fieldType.toLowerCase().contains(item)) {
                    convertTypeName = PROPERTIES.getProperty(item);
                    break;
                }
            }
        }
        // 判断是否是枚举字段
        if (GenCommonUtil.isEnumField(metaInfo.getRemarks())) {
            columnType = new IColumnType() {
                @Override
                public String getType() {
                    ColumnInfo columnInfo = ColumnInfo.build(metaInfo);
                    RemarksEnumInfo enumInfo = RemarksEnumInfo.build(columnInfo, globalConfig);
                    // 你想修改的类型
                    return enumInfo.getEnumFileName();
                }

                @Override
                public String getPkg() {
                    // 类型所在包
                    return GenConstant.JASON_PHOTOGRAPHY_DAO_ENUM_PACKAGE;
                }
            };
        }

//        System.out.println("mysql类型->" + fieldType + "，java类型->" + convertTypeName);
        if (StrUtil.isNotBlank(convertTypeName) && columnType == null) {
            String finalConvertTypeName = convertTypeName;
            columnType = new IColumnType() {
                @Override
                public String getType() {
                    // 你想修改的类型
                    return finalConvertTypeName;
                }

                @Override
                public String getPkg() {
                    // 类型所在包
                    return null;
                }
            };
        }
        return columnType == null ? typeRegistry.getColumnType(metaInfo) : columnType;
    }

    /**
     * 生成枚举文件
     *
     * @param remarksEnumInfo 列注释枚举信息
     */
    private void createEnumFile(RemarksEnumInfo remarksEnumInfo) {
        Map<String, Object> dataMap = BeanUtil.beanToMap(remarksEnumInfo);
        try {
//            System.out.println(JSONUtil.toJsonPrettyStr(dataMap));
            String projectPath = GenPhotographyRun.GEN_CONFIG_DATA.getProjectPath();
            String outputFilePath = projectPath + SEPARATOR
                    + GenConstant.PROJECT_MODULES_NAME + SEPARATOR
                    + GenConstant.JASON_PHOTOGRAPHY_DAO + SEPARATOR
                    + GenConstant.JAVA_PATH + SEPARATOR
                    + GenConstant.JASON_PHOTOGRAPHY_DAO_ENUM_PACKAGE_PATH + SEPARATOR
                    + remarksEnumInfo.getEnumFileName() + ".java";
            if (FileUtil.exist(outputFilePath)) {
                return;
            }
            // 生成枚举类
            File outputFile = FileUtil.touch(outputFilePath);
//            System.out.println("outputFilePath->" + outputFilePath);
            String templatePath = projectPath + SEPARATOR + GenConstant.JASON_GEN_TEMPLATES_PATH;
            File templateFile = new File(templatePath);
//            System.out.println("templatePath->" + templatePath);
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
            configuration.setDirectoryForTemplateLoading(templateFile);
            Template t = configuration.getTemplate("EnumP.ftl", StandardCharsets.UTF_8.toString());
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8), 10240);
            t.process(dataMap, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
