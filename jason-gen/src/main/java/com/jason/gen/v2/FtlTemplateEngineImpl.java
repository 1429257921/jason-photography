package com.jason.gen.v2;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
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
     *
     * @param genArgs 生成器配置参数对象
     * @return
     * @throws Exception
     */
    @Override
    public List<TemplateDefinition> loadTemplates(@NotNull GenArgs genArgs) throws Exception {
        List<TemplateDefinition> templateDefinitionList = new ArrayList<>(32);
        String templatePath = genArgs.getProjectPath() + Constant.SEPARATOR + Constant.JASON_GEN_TEMPLATES_PATH;
        for (TemplateFileNameEnum templateFileNameEnum : TemplateFileNameEnum.values()) {
            TemplateDefinition definition = new TemplateDefinition();
            String templateFileName = templateFileNameEnum.name();
            String fullTemplateFileName = templateFileNameEnum.name() + ".ftl";
            definition.setTemplateFileName(templateFileName);
            definition.setFullTemplateFileName(fullTemplateFileName);
            definition.setBaseTemplateFilePath(templatePath);
            if (!FileUtil.exist(templatePath + Constant.SEPARATOR + fullTemplateFileName)) {
                throw new Exception("模板文件【" + fullTemplateFileName + "】在目录：" + templatePath + " 下未找到！");
            }
            templateDefinitionList.add(definition);
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
