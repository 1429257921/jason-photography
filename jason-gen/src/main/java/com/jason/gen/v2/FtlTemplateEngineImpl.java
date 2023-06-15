package com.jason.gen.v2;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReflectUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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
     * @param genArgs 生成器配置参数对象
     * @return
     * @throws Exception
     */
    @Override
    public List<TemplateDefinition> loadTemplates(@NotNull GenArgs genArgs) throws Exception {
        List<TemplateDefinition> templateDefinitionList = new ArrayList<>(32);
        ConcurrentMap<ServiceNameEnum, ServiceGenConfig> serviceGenConfigMap = genArgs.getConvertData().getServiceGenConfigMap();
        for (ServiceNameEnum serviceNameEnum : serviceGenConfigMap.keySet()) {
            ServiceGenConfig serviceGenConfig = serviceGenConfigMap.get(serviceNameEnum);
            for (TemplateFileNameEnum templateFileNameEnum : TemplateFileNameEnum.values()) {
                String templateFileName = templateFileNameEnum.name();
                String tfn = "gen" + templateFileName.substring(0, templateFileName.length() - 1);
                Field[] fieldArr = ReflectUtil.getFields(serviceGenConfig.getClass(), field -> !field.getName().contains("Path"));
                for (Field field : fieldArr) {
//                    field.setAccessible(true);
                    String fieldName = field.getName();
                    if (fieldName.equals(tfn) && Boolean.TRUE.equals(field.get(serviceGenConfig))) {
                        String templatePath = genArgs.getProjectPath() + Constant.SEPARATOR + Constant.JASON_GEN_TEMPLATES_PATH;
                        TemplateDefinition templateDefinition = new TemplateDefinition();
                        templateDefinition.setTemplateFileName(templateFileName + ".ftl");
                        templateDefinition.setBaseTemplateFilePath(templatePath);
                        Object baseOutputFilePath = ReflectUtil.getFieldValue(serviceGenConfig, fieldName + "Path");
                        templateDefinition.setBaseOutputFilePath((String) baseOutputFilePath);
                        templateDefinitionList.add(templateDefinition);
                    }
                }
            }
        }
        return templateDefinitionList;
    }

    /**
     * 主要填充
     *
     * @param templateDefinitionList 模板定义对象数组
     * @param tableDefinitionMap
     * @param typeConvertMap
     * @throws Exception
     */
    @Override
    public void populateTemplateDefinition(@NotNull List<TemplateDefinition> templateDefinitionList,
                                           @NotNull ConcurrentMap<String, TableDefinition> tableDefinitionMap,
                                           @NotNull ConcurrentMap<String, TypeConvertDefinition> typeConvertMap) throws Exception {
        for (TemplateDefinition templateDefinition : templateDefinitionList) {
            // 对文件生成位置的填充
        }
    }

    @Override
    public void outputFile(@NotNull List<TemplateDefinition> templateDefinitionList) throws Exception {
        for (TemplateDefinition templateDefinition : templateDefinitionList) {
            File outputFile = FileUtil.touch(templateDefinition.getBaseTemplateFilePath());
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
            configuration.setDirectoryForTemplateLoading(outputFile);
            // 获取模板对象
            Template template = configuration.getTemplate(templateDefinition.getTemplateFileName(), Constant.CHARSET_STR);
            // 获取写入缓存流
            BufferedWriter bufferedWriter = FileUtil.getWriter(outputFile, Constant.CHARSET, true);
            // 执行程序
            template.process(templateDefinition.getTemplatePopulateData(), bufferedWriter);
            // 关闭流
            bufferedWriter.close();
        }
    }
}
