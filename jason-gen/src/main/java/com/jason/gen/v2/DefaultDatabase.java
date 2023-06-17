package com.jason.gen.v2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * 默认数据库实现类，使用{@link SqlDataDefinitionParse}去解析DDL文本，获取到表定义和列定义信息
 * 获取表定义信息调用{@link SqlDataDefinitionParse#parseTableDefinition(String)}
 * 获取列定义信息调用{@link SqlDataDefinitionParse#parseColumnDefinition(String)}
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
@SuppressWarnings("all")
public class DefaultDatabase extends AbstractDatabase {

    @Override
    protected void loadTableCreateSqlText(String tableName,
                                          Connection connection,
                                          ConcurrentMap<String, String> tableCreateMap) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SHOW CREATE TABLE " + tableName);
        if (rs != null && rs.next()) {
            tableCreateMap.put(tableName, rs.getString(2));
            rs.close();
        }
        statement.close();
    }

    @Override
    protected void getTableDefinition(String tableName,
                                      ConcurrentMap<String, TableDefinition> tableDefinitionMap,
                                      ConcurrentMap<String, String> tableCreateMap) throws Exception {
        for (String key : tableCreateMap.keySet()) {
            TableDefinition tableDefinition = SqlDataDefinitionParse.parseTableDefinition(tableCreateMap.get(key));
            tableDefinitionMap.put(key, tableDefinition);
        }
    }

    @Override
    protected void getColumnDefinition(String tableName,
                                       ConcurrentMap<String, TableDefinition> tableDefinitionMap,
                                       ConcurrentMap<String, String> tableCreateMap) throws Exception {
        for (String key : tableCreateMap.keySet()) {
            List<ColumnDefinition> columnDefinitionList = SqlDataDefinitionParse.parseColumnDefinition(tableCreateMap.get(key));
            tableDefinitionMap.get(key).setColumnDefinitionList(columnDefinitionList);
        }
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
