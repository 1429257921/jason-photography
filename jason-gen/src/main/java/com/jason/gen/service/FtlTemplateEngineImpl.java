package com.jason.gen.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.jason.gen.constant.Constant;
import com.jason.gen.entity.*;
import com.jason.gen.enums.ServiceNameEnum;
import com.jason.gen.enums.TemplateFileNameEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * freemarker模板引擎实现类
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
public class FtlTemplateEngineImpl implements TemplateEngine {

    @Override
    public ConcurrentMap<TemplateFileNameEnum, TemplateDefinition> loadTemplates(@NotNull GenArgs genArgs) throws Exception {
        ConcurrentMap<TemplateFileNameEnum, TemplateDefinition> templateDefinitionMap = new ConcurrentHashMap<>(16);
        String templatePath = genArgs.getProjectPath() + Constant.SEPARATOR + Constant.JASON_GEN_TEMPLATES_PATH;
        for (TemplateFileNameEnum templateFileNameEnum : TemplateFileNameEnum.values()) {
            TemplateDefinition definition = new TemplateDefinition();
            String templateFileName = templateFileNameEnum.name();
            String fullTemplateFileName = templateFileName + ".ftl";
            definition.setTemplateFileName(templateFileName);
            definition.setFullTemplateFileName(fullTemplateFileName);
            definition.setBaseTemplateFilePath(templatePath);
            definition.setJavaClassNameSuffix(templateFileNameEnum.getJavaClassNameSuffix());
            if (!FileUtil.exist(templatePath + Constant.SEPARATOR + fullTemplateFileName)) {
                throw new Exception("模板文件【" + fullTemplateFileName + "】在目录：" + templatePath + " 下未找到！");
            }
            templateDefinitionMap.put(templateFileNameEnum, definition);
        }
        return templateDefinitionMap;
    }

    /**
     * 填充将需要共用的数据进行互相填充
     *
     * @param templateDefinitionMap   模板定义对象数组
     * @param tableDefinitionMap      表定义信息集合
     * @param typeConvertMap          类型转换信息集合
     * @param outputFileDefinitionMap 输出文件定义信息集合
     */
    @Override
    public void populateTemplateDefinition(@NotNull ConcurrentMap<TemplateFileNameEnum, TemplateDefinition> templateDefinitionMap,
                                           @NotNull ConcurrentMap<String, TableDefinition> tableDefinitionMap,
                                           @NotNull ConcurrentMap<String, TypeConvertDefinition> typeConvertMap,
                                           @NotNull ConcurrentMap<ServiceNameEnum, List<OutputFileDefinition>> outputFileDefinitionMap) {
        TemplatePlaceholderData commonTemplatePlaceholderData = null;
        for (List<OutputFileDefinition> outputFileDefinitionList : outputFileDefinitionMap.values()) {
            for (OutputFileDefinition outputFileDefinition : outputFileDefinitionList) {
                TemplatePlaceholderData populateData = outputFileDefinition.getPopulateData();
                if (commonTemplatePlaceholderData == null) {
                    commonTemplatePlaceholderData = populateData;
                }
                CopyOptions copyOptions = CopyOptions.create().setOverride(false);
                copyOptions.setIgnoreProperties("enumClassName", "packageEnum", "enumColumnDefinition");
                BeanUtil.copyProperties(populateData, commonTemplatePlaceholderData, copyOptions);
            }
        }
        // 填充完整数据
        if (commonTemplatePlaceholderData != null) {
            for (List<OutputFileDefinition> outputFileDefinitionList : outputFileDefinitionMap.values()) {
                for (OutputFileDefinition outputFileDefinition : outputFileDefinitionList) {
                    TemplatePlaceholderData tempCommonTemplatePlaceholderData = BeanUtil.copyProperties(commonTemplatePlaceholderData,
                            TemplatePlaceholderData.class, "enumClassName", "packageEnum", "enumColumnDefinition");
                    TemplatePlaceholderData populateData = outputFileDefinition.getPopulateData();
                    tempCommonTemplatePlaceholderData.setPackageEnum(populateData.getPackageEnum());
                    tempCommonTemplatePlaceholderData.setModuleName(populateData.getModuleName());
                    tempCommonTemplatePlaceholderData.setEnumClassName(populateData.getEnumClassName());
                    tempCommonTemplatePlaceholderData.setEnumColumnDefinition(populateData.getEnumColumnDefinition());
                    outputFileDefinition.setPopulateData(tempCommonTemplatePlaceholderData);
                }
            }
        }
    }

    @Override
    public void outputFile(@NotNull GenArgs genArgs, @NotNull ConcurrentMap<ServiceNameEnum, List<OutputFileDefinition>> outputFileDefinitionMap) {
        // 生成成功的文件完整磁盘路径
        List<String> alreadyGenFilePathList = new ArrayList<>(32);
        // 是否生成失败
        boolean isError = false;
        // 遍历模块
        outermost:
        for (List<OutputFileDefinition> outputFileDefinitionList : outputFileDefinitionMap.values()) {
            // 遍历模块下需要生成的文件
            for (OutputFileDefinition outputFileDefinition : outputFileDefinitionList) {
                try {
                    String baseOutputFilePath = outputFileDefinition.getBaseOutputFilePath();
                    String fullOutputFileName = outputFileDefinition.getFullOutputFileName();
                    String fullOutputFilePath = baseOutputFilePath + Constant.SEPARATOR + fullOutputFileName;
                    fullOutputFilePath = fullOutputFilePath.replace("/", Constant.SEPARATOR).replace("\\", Constant.SEPARATOR).replace("\\\\", Constant.SEPARATOR);
                    boolean exist = FileUtil.exist(fullOutputFilePath);
                    if (!genArgs.getOverwriteFile() && exist) {
                        continue;
                    }
                    TemplateDefinition templateDefinition = outputFileDefinition.getTemplateDefinition();
                    // 不存在则创建
                    File baseTemplateFile = FileUtil.touch(templateDefinition.getBaseTemplateFilePath());
                    // 创建输出配置
                    Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
                    configuration.setDirectoryForTemplateLoading(baseTemplateFile);
                    // 获取模板对象
                    Template template = configuration.getTemplate(templateDefinition.getFullTemplateFileName(), Constant.CHARSET_STR);
                    FileUtil.del(fullOutputFilePath);
                    ThreadUtil.sleep(100);
                    // 获取写入缓存流
                    BufferedWriter bufferedWriter = FileUtil.getWriter(fullOutputFilePath, Constant.CHARSET, true);
                    // 执行程序
                    TemplatePlaceholderData populateData = outputFileDefinition.getPopulateData();
                    alreadyGenFilePathList.add(fullOutputFilePath);
                    System.out.println("模块名->" + populateData.getModuleName() + "，文件名->" + fullOutputFilePath + "正在生成...");
                    template.process(populateData, bufferedWriter);
                    // 关闭流
                    bufferedWriter.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                    isError = true;
                    break outermost;
                }
            }
        }
        // 如果有一个文件生成失败，则删除所有已经生成的文件
        if (isError) {
            ThreadUtil.sleep(1000);
            for (String alreadyGenFilePath : alreadyGenFilePathList) {
                if (StrUtil.isNotBlank(alreadyGenFilePath)) {
                    FileUtil.del(alreadyGenFilePath);
                    System.out.println("删除文件->" + alreadyGenFilePath);
                }
            }

        }
    }
}
