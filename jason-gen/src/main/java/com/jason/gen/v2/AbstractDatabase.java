package com.jason.gen.v2;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/13
 **/
public abstract class AbstractDatabase {

    protected Connection connection;

    protected ConcurrentMap<String, TableDefinition> tableDefinitionMap = new ConcurrentHashMap<>(128);

    public ConcurrentMap<String, TableDefinition> init(GenArgs genArgs) throws Exception {
        // 校验驱动类是否存在
        this.check(genArgs.getDatabaseDriverClassName());
        // 获取连接
        this.getConnection(genArgs);
        try {
            GenArgs.ConvertData convertData = genArgs.getConvertData();
            for (String tableName : convertData.getTableList()) {
                // 获取表定义
                this.getTableDefinition(tableName, this.tableDefinitionMap);
                // 获取列定义
                this.getColumnDefinition(tableName, this.tableDefinitionMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return this.tableDefinitionMap;
    }

    protected void getTableDefinition(String tableName, ConcurrentMap<String, TableDefinition> tableDefinitionMap) throws Exception {
        try {
            //获取数据库的元数据
            DatabaseMetaData db = connection.getMetaData();
            TableDefinition tableDefinition = new TableDefinition();
            //从元数据中获取到所有的表名
            ResultSet rs = db.getTables(null, null, tableName, new String[]{"TABLE"});
            while (rs.next()) {
                String queryTableName = rs.getString(3);
                if (StrUtil.isBlank(queryTableName)) {
                    throw new Exception("表【" + tableName + "】不存在");
                }
                if (queryTableName.equals(tableName)) {
                    tableDefinition.setTableName(tableName);
                    tableDefinition.setTableComment("");
                    tableDefinitionMap.put(tableName, tableDefinition);
                }
            }
        } catch (Exception e) {
            throw new Exception("获取表定义失败", e);
        }
    }

    protected void getColumnDefinition(String tableName, ConcurrentMap<String, TableDefinition> tableDefinitionMap) throws Exception {
        List<Map<String, String>> list = new ArrayList<>(32);
        try {
            //结果集元数据
            ResultSet resultSet = connection.getMetaData().getColumns(null, "%", tableName, "%");
            while (resultSet.next()) {
                Map<String, String> map = new HashMap<>(32);
                for (DataBaseColumnDefinitionEnum value : DataBaseColumnDefinitionEnum.values()) {
                    map.put(value.name(), resultSet.getString(value.name()));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<ColumnDefinition> columnDefinitionList = new ArrayList<>(32);
        for (Map<String, String> obj : list) {
            JSONObject jsonObject = JSONUtil.createObj();
            for (DataBaseColumnDefinitionEnum dataBaseColumnDefinitionEnum : DataBaseColumnDefinitionEnum.values()) {
                for (String key : obj.keySet()) {
                    if (key.equalsIgnoreCase(dataBaseColumnDefinitionEnum.name())) {
                        String javaFieldName = dataBaseColumnDefinitionEnum.getJavaFieldName();
                        String value = obj.get(key);
                        jsonObject.set(javaFieldName, value);
                    }
                }
            }
            columnDefinitionList.add(jsonObject.toBean(ColumnDefinition.class));
        }
        TableDefinition tableDefinition = tableDefinitionMap.get(tableName);
        tableDefinition.setColumnDefinitionsList(columnDefinitionList);
    }

    protected void getConnection(GenArgs genArgs) throws Exception {
        try {
            System.out.println("数据库连接中...");
            this.connection = DriverManager.getConnection(
                    genArgs.getDatabaseUrl(),
                    genArgs.getDatabaseUsername(),
                    genArgs.getDatabasePassword());
        } catch (SQLException e) {
            throw new Exception("连接数据库失败", e);
        }
        System.out.println("数据库已连接");
    }

    protected void closeConnection() throws Exception {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new Exception("关闭数据库连接通道失败", e);
        }
    }

    private void check(String driverClassName) throws Exception {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new Exception("未找到JDBC驱动类->" + driverClassName);
        }
    }

    /**
     * 数据库返回的列定义信息属性名称枚举
     */
    @Getter
    @AllArgsConstructor
    @SuppressWarnings("all")
    protected enum DataBaseColumnDefinitionEnum {
        COLUMN_NAME("columnName"),
        REMARKS("columnComment"),
        COLUMN_SIZE("length"),
        NUM_PREC_RADIX("numPrecRadix"),
        COLUMN_DEF("defaultValue"),
        TYPE_NAME("jdbcTypeName"),
        NULLABLE("nullable"),
        DATA_TYPE("dataType"),

        TABLE_CAT("tableCat"),
        TABLE_NAME("tableName"),
        TABLE_SCHEM("tableSchem"),

        BUFFER_LENGTH("bufferLength"),
        CHAR_OCTET_LENGTH("charOctetLength"),
        DECIMAL_DIGITS("decimalDigits"),
        IS_AUTOINCREMENT("isAutoincrement"),
        IS_GENERATEDCOLUMN("isGeneratedColumn"),
        IS_NULLABLE("isNullable"),
        ORDINAL_POSITION("ordinalPosition"),

        SCOPE_CATALOG("scopeCatalog"),
        SCOPE_SCHEMA("scopeSchema"),
        SCOPE_TABLE("scopeTable"),

        SOURCE_DATA_TYPE("sourceDataType"),
        SQL_DATA_TYPE("sql_dataType"),
        SQL_DATETIME_SUB("sqlDatetimeSub");

        /**
         * bean类属性名称
         */
        private final String javaFieldName;
    }

}
