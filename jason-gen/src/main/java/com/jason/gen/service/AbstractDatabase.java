package com.jason.gen.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jason.gen.entity.ColumnDefinition;
import com.jason.gen.entity.GenArgs;
import com.jason.gen.entity.TableDefinition;
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
 * 公共抽象数据库引擎
 * 主要职责：
 * 1、加载驱动，通过Class.forName(driverClassName)去加载。
 * 2、获取连接通道，通过DriverManager.getConnection()去获取。
 * 3、加载表DDL信息，Mysql中可通过执行SHOW CREATE TABLE + 表名去查询。
 * 4、加载表定义信息，填充tableDefinitionMap.TableDefinition对象
 * 5、加载列定义信息，填充tableDefinitionMap.TableDefinition.columnDefinitionsList对象
 *
 * @author guozhongcheng
 * @since 2023/6/13
 **/
public abstract class AbstractDatabase {
    /**
     * 数据库连接对象
     */
    protected Connection connection;
    /**
     * 表DDL文本集合
     */
    protected final ConcurrentMap<String, String> tableCreateMap = new ConcurrentHashMap<>(16);
    /**
     * 表定义信息集合
     */
    protected final ConcurrentMap<String, TableDefinition> tableDefinitionMap = new ConcurrentHashMap<>(16);

    /**
     * 初始化
     *
     * @param genArgs 生成器配置参数对象
     * @return 表定义信息集合
     * @throws Exception 数据库异常
     */
    public ConcurrentMap<String, TableDefinition> init(GenArgs genArgs) throws Exception {
        // 加载数据库驱动类
        this.loadDriver(genArgs.getDatabaseDriverClassName());
        // 获取连接
        this.getConnection(genArgs);
        try {
            // 获取需要生成的表名称集合
            GenArgs.ConvertData convertData = genArgs.getConvertData();
            for (String tableName : convertData.getTableList()) {
                // 加载表创建信息(DDL文本)
                this.loadTableCreateSqlText(tableName, connection, tableCreateMap);
                // 获取表定义
                this.getTableDefinition(tableName, this.tableDefinitionMap, this.tableCreateMap);
                // 获取列定义
                this.getColumnDefinition(tableName, this.tableDefinitionMap, this.tableCreateMap);
            }
        } finally {
            this.closeConnection();
        }
        return this.tableDefinitionMap;
    }

    /**
     * 加载表DDL文本
     *
     * @param tableName      表名称
     * @param connection     数据库连接对象
     * @param tableCreateMap 表DDL文本集合
     * @throws SQLException sql异常
     */
    protected void loadTableCreateSqlText(String tableName, Connection connection, ConcurrentMap<String, String> tableCreateMap) throws SQLException {
        // 如有需要请重写
    }

    /**
     * 获取表定义信息
     *
     * @param tableName          表名称
     * @param tableDefinitionMap 表定义信息集合
     * @param tableCreateMap     表DDL文本集合
     * @throws Exception 异常
     */
    protected void getTableDefinition(String tableName,
                                      ConcurrentMap<String, TableDefinition> tableDefinitionMap,
                                      ConcurrentMap<String, String> tableCreateMap) throws Exception {
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

    /**
     * 获取列定义信息
     *
     * @param tableName          表名称
     * @param tableDefinitionMap 表定义信息集合
     * @param tableCreateMap     表DDL文本集合
     * @throws Exception 异常
     */
    protected void getColumnDefinition(String tableName,
                                       ConcurrentMap<String, TableDefinition> tableDefinitionMap,
                                       ConcurrentMap<String, String> tableCreateMap) throws Exception {
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
        tableDefinition.setColumnDefinitionList(columnDefinitionList);
    }

    /**
     * 获取数据库连接
     *
     * @param genArgs 生成器配置参数对象
     * @throws Exception 异常
     */
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

    /**
     * 关闭数据库连接
     *
     * @throws Exception 异常
     */
    protected void closeConnection() throws Exception {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new Exception("关闭数据库连接通道失败", e);
        }
    }

    /**
     * 加载驱动类
     *
     * @param driverClassPackage 驱动类包路径
     * @throws Exception 加载失败异常
     */
    private void loadDriver(String driverClassPackage) throws Exception {
        try {
            Class.forName(driverClassPackage);
        } catch (ClassNotFoundException e) {
            throw new Exception("未找到JDBC驱动类->" + driverClassPackage);
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
