package com.jason.gen.v2;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
public interface TemplateEngine {


    /**
     * 加载模板文件
     *
     * @param genArgs 生成器配置参数对象
     * @return List<TemplateDefinition>
     * @throws Exception 异常
     */
    ConcurrentMap<TemplateFileNameEnum, TemplateDefinition> loadTemplates(@NotNull GenArgs genArgs) throws Exception;

    /**
     * 填充模板定义参数
     *
     * @param templateDefinitionMap   模板定义对象数组
     * @param tableDefinitionMap
     * @param typeConvertMap
     * @param outputFileDefinitionMap
     * @throws Exception 异常
     */
    void populateTemplateDefinition(@NotNull ConcurrentMap<TemplateFileNameEnum, TemplateDefinition> templateDefinitionMap,
                                    @NotNull ConcurrentMap<String, TableDefinition> tableDefinitionMap,
                                    @NotNull ConcurrentMap<String, TypeConvertDefinition> typeConvertMap,
                                    @NotNull ConcurrentMap<ServiceNameEnum, List<OutputFileDefinition>> outputFileDefinitionMap) throws Exception;

    /**
     * 输出文件
     *
     * @param genArgs 模板定义对象数组
     * @param outputFileDefinitionMap 模板定义对象数组
     * @throws Exception 异常
     */
    void outputFile(@NotNull GenArgs genArgs,
                    @NotNull ConcurrentMap<ServiceNameEnum, List<OutputFileDefinition>> outputFileDefinitionMap) throws Exception;


}
