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
    List<TemplateDefinition> loadTemplates(@NotNull GenArgs genArgs) throws Exception;

    /**
     * 填充模板定义参数
     *
     * @param templateDefinitionList 模板定义对象数组
     * @param tableDefinitionMap
     * @param typeConvertMap
     * @throws Exception 异常
     */
    void populateTemplateDefinition(@NotNull List<TemplateDefinition> templateDefinitionList,
                                    @NotNull ConcurrentMap<String, TableDefinition> tableDefinitionMap,
                                    @NotNull ConcurrentMap<String, TypeConvertDefinition> typeConvertMap) throws Exception;

    /**
     * 输出文件
     *
     * @param templateDefinitionList 模板定义对象数组
     * @throws Exception 异常
     */
    void outputFile(@NotNull List<TemplateDefinition> templateDefinitionList) throws Exception;


}
