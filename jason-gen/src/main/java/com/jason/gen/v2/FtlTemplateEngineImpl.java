package com.jason.gen.v2;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
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
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
public class FtlTemplateEngineImpl implements TemplateEngine {


    /**
     * 构建需要
     *
     * @param genArgs 生成器配置参数对象
     * @return
     * @throws Exception
     */
    @Override
    public ConcurrentMap<TemplateFileNameEnum, TemplateDefinition> loadTemplates(@NotNull GenArgs genArgs) throws Exception {
        ConcurrentMap<TemplateFileNameEnum, TemplateDefinition> templateDefinitionMap = new ConcurrentHashMap<>(16);
        String templatePath = genArgs.getProjectPath() + Constant.SEPARATOR + Constant.JASON_GEN_TEMPLATES_PATH;
        for (TemplateFileNameEnum templateFileNameEnum : TemplateFileNameEnum.values()) {
            TemplateDefinition definition = new TemplateDefinition();
            String templateFileName = templateFileNameEnum.name();
            String fullTemplateFileName = templateFileNameEnum.name() + ".ftl";
            definition.setTemplateFileName(templateFileName);
            definition.setFullTemplateFileName(fullTemplateFileName);
            definition.setBaseTemplateFilePath(templatePath);
            definition.setJavaClassNameSuffix(TemplateFileNameEnum.getJavaClassNameSuffix(templateFileNameEnum));
            if (!FileUtil.exist(templatePath + Constant.SEPARATOR + fullTemplateFileName)) {
                throw new Exception("模板文件【" + fullTemplateFileName + "】在目录：" + templatePath + " 下未找到！");
            }
            templateDefinitionMap.put(templateFileNameEnum, definition);
        }
        return templateDefinitionMap;
    }

    /**
     * 主要填充
     *
     * @param templateDefinitionMap 模板定义对象数组
     * @param tableDefinitionMap
     * @param typeConvertMap
     * @throws Exception
     */
    @Override
    public void populateTemplateDefinition(@NotNull ConcurrentMap<TemplateFileNameEnum, TemplateDefinition> templateDefinitionMap, @NotNull ConcurrentMap<String, TableDefinition> tableDefinitionMap, @NotNull ConcurrentMap<String, TypeConvertDefinition> typeConvertMap, @NotNull ConcurrentMap<ServiceNameEnum, List<OutputFileDefinition>> outputFileDefinitionMap) throws Exception {

        TemplatePopulateData commonTemplatePopulateData = null;
        for (List<OutputFileDefinition> outputFileDefinitionList : outputFileDefinitionMap.values()) {
            for (OutputFileDefinition outputFileDefinition : outputFileDefinitionList) {
                TemplatePopulateData populateData = outputFileDefinition.getPopulateData();
                if (commonTemplatePopulateData == null) {
                    commonTemplatePopulateData = populateData;
                }
                CopyOptions copyOptions = CopyOptions.create().setOverride(false);
                copyOptions.setIgnoreProperties("enumClassName", "packageEnum", "enumColumnDefinition");
                BeanUtil.copyProperties(populateData, commonTemplatePopulateData, copyOptions);
            }
        }
        // 填充完整数据
        if (commonTemplatePopulateData != null) {
            for (List<OutputFileDefinition> outputFileDefinitionList : outputFileDefinitionMap.values()) {
                for (OutputFileDefinition outputFileDefinition : outputFileDefinitionList) {
                    TemplatePopulateData tempCommonTemplatePopulateData = BeanUtil.copyProperties(commonTemplatePopulateData, TemplatePopulateData.class,"enumClassName", "packageEnum", "enumColumnDefinition");
                    TemplatePopulateData populateData = outputFileDefinition.getPopulateData();
                    tempCommonTemplatePopulateData.setPackageEnum(populateData.getPackageEnum());
                    tempCommonTemplatePopulateData.setEnumClassName(populateData.getEnumClassName());
                    tempCommonTemplatePopulateData.setEnumColumnDefinition(populateData.getEnumColumnDefinition());
                    outputFileDefinition.setPopulateData(tempCommonTemplatePopulateData);
                }
            }
        }
    }

    @Override
    public void outputFile(@NotNull GenArgs genArgs, @NotNull ConcurrentMap<ServiceNameEnum, List<OutputFileDefinition>> outputFileDefinitionMap) throws Exception {
        List<String> alreadyGenFilePathList = new ArrayList<>(32);
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
                    TemplatePopulateData populateData = outputFileDefinition.getPopulateData();
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
