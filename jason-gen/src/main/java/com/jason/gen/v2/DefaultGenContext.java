package com.jason.gen.v2;

/**
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/13
 **/
public class DefaultGenContext extends AbstractGenContext {

    public DefaultGenContext() {
        super(new DefaultDatabase(), new FtlTemplateEngineImpl());
    }

    public DefaultGenContext(AbstractDatabase abstractDatabase, TemplateEngine templateEngine) {
        super(abstractDatabase, templateEngine);
    }

    @Override
    public void build() throws Exception {
        super.build();
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
    protected void populateTemplateDefinition() throws Exception {
        super.populateTemplateDefinition();
    }

    @Override
    protected void loadTemplates(GenArgs genArgs) throws Exception {
        super.loadTemplates(genArgs);
    }

    @Override
    protected void prepareBuild() {
        super.prepareBuild();
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
