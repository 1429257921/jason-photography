package com.jason.gen.service;

import com.jason.gen.entity.GenArgs;

/**
 * 默认生成器上下文容器实现类
 *
 * @author guozhongcheng
 * @since 2023/6/13
 **/
public class DefaultGenContext extends AbstractGenContext {

    /**
     * 注入默认数据库引擎和Freemarker模板引擎
     */
    public DefaultGenContext() {
        super(new DefaultDatabase(), new FtlTemplateEngineImpl());
    }

    @Override
    public void run() throws Exception {
        super.run();
    }

    @Override
    protected void executeCodeGeneration() throws Exception {
        super.executeCodeGeneration();
    }

    @Override
    protected void lastCheck() {
        super.lastCheck();
    }

    @Override
    protected void populateTemplatePlaceholderData() throws Exception {
        super.populateTemplatePlaceholderData();
    }

    @Override
    protected void loadTemplates(GenArgs genArgs) throws Exception {
        super.loadTemplates(genArgs);
    }

    @Override
    protected void prepareRun() {
        super.prepareRun();
    }

    @Override
    protected void loadConfiguration() throws Exception {
        super.loadConfiguration();
    }

    @Override
    protected void parseConfiguration() throws Exception {
        super.parseConfiguration();
    }

    @Override
    protected void loadDataBaseTableInfo(GenArgs genArgs) throws Exception {
        super.loadDataBaseTableInfo(genArgs);
    }

    @Override
    protected void populateColumnTypeConverter() {
        super.populateColumnTypeConverter();
    }
}
