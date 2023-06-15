package com.jason.gen.v2;

import java.util.concurrent.ConcurrentMap;

/**
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
public class DefaultDatabase extends AbstractDatabase {
    @Override
    protected void getTableDefinition(String tableName, ConcurrentMap<String, TableDefinition> tableDefinitionMap) throws Exception {
        super.getTableDefinition(tableName, tableDefinitionMap);
    }

    @Override
    protected void getColumnDefinition(String tableName, ConcurrentMap<String, TableDefinition> tableDefinitionMap) throws Exception {
        super.getColumnDefinition(tableName, tableDefinitionMap);
    }

    @Override
    protected void getConnection(GenArgs genArgs) throws Exception {
        super.getConnection(genArgs);
    }

    @Override
    protected void closeConnection() throws Exception {
        super.closeConnection();
    }
}
