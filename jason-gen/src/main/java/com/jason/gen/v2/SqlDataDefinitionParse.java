package com.jason.gen.v2;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * sql DDL文本数据解析工具类
 * 示例:
 * CREATE TABLE `k_user_login_record` (
 * `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
 * `user_id` int(11) NOT NULL COMMENT '用户主键ID',
 * `auth_id` int(11) NOT NULL COMMENT '用户登录授权主键ID',
 * `login_status` tinyint(1) NOT NULL COMMENT '操作类型（0、登录成功，1、登出成功，2、登录失败，3、登出失败）',
 * `app_version` varchar(32) DEFAULT NULL COMMENT '客户端版本号',
 * `mac` varchar(64) DEFAULT NULL COMMENT '设备硬件地址',
 * `ip` varchar(32) NOT NULL COMMENT '登录ip',
 * `os` varchar(16) DEFAULT NULL COMMENT '登录系统，IOS等',
 * `os_version` varchar(32) DEFAULT NULL COMMENT '系统版本',
 * `remark` varchar(200) NOT NULL DEFAULT '' COMMENT '备注',
 * `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0、正常，1、删除）',
 * `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 * PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='坤坤云用户登录记录表'
 *
 * @author guozhongcheng
 * @since 2023/6/16
 **/
@SuppressWarnings("all")
public abstract class SqlDataDefinitionParse {

    /**
     * 将sqlDDL文本内容解析成表定义对象
     *
     * @param sqlDataDefinitionText sqlDDL文本
     * @return 表定义对象
     */
    public static TableDefinition parseTableDefinition(String sqlDataDefinitionText) {
        TableDefinition tableDefinition = new TableDefinition();
        String[] splitSqlDataDefinitionText = splitSqlDataDefinitionText(sqlDataDefinitionText);
        // 获取表信息
        getTableInfo(sqlDataDefinitionText, tableDefinition);
        return tableDefinition;
    }
    public static List<ColumnDefinition> parseColumnDefinition(String sqlDataDefinitionText) {
        List<ColumnDefinition> columnDefinitionsList = new ArrayList<>(64);
        String[] splitSqlDataDefinitionText = splitSqlDataDefinitionText(sqlDataDefinitionText);
        String keyColumnName = "";
        // 获取列信息
        for (String line : splitSqlDataDefinitionText) {
            line = line.trim();
            ColumnDefinition columnDefinition = new ColumnDefinition();
            if (StrUtil.isNotBlank(getKeyColumnName(line))) {
                keyColumnName = getKeyColumnName(line);
            }
            if (line.contains("COMMENT")) {
                columnDefinition.setColumnName(getColumnName(line));
                columnDefinition.setColumnComment(getColumnComment(line));
                columnDefinition.setJdbcTypeName(getColumnTypeName(line));
                columnDefinition.setNullable(getColumnIsNull(line));
                columnDefinition.setLength(getColumnTypeLength(line));
                columnDefinition.setAutoincrement(getColumnIsAuto(line));
                columnDefinition.setDefaultValue(getColumnDefaultValue(line));
                columnDefinition.setKeyFlag(false);
                columnDefinitionsList.add(columnDefinition);
            }
        }
        if (StrUtil.isNotBlank(keyColumnName)) {
            String finalKeyColumnName = keyColumnName;
            columnDefinitionsList.forEach(item -> {
                if (finalKeyColumnName.equalsIgnoreCase(item.getColumnName())) {
                    item.setKeyFlag(true);
                }
            });
        }
        return columnDefinitionsList;
    }

    private static String[] splitSqlDataDefinitionText(String sqlDataDefinitionText) {
        String substring = sqlDataDefinitionText.substring(sqlDataDefinitionText.indexOf("(") + 1, sqlDataDefinitionText.lastIndexOf(")")).trim();
        return Arrays.stream(substring.split("\\n")).map(n -> n = n.trim()).toArray(String[]::new);
    }

    private static void getTableInfo(String sqlDataDefinitionText, TableDefinition tableDefinition) {
        String[] split = sqlDataDefinitionText.split("\\n");
        int length = split.length;
        if (StrUtil.isNotBlank(getTableName(split[0]))) {
            tableDefinition.setTableName(getTableName(split[0]));
        }
        if (StrUtil.isNotBlank(getTableNameComment(split[length - 1]))) {
            tableDefinition.setTableComment(getTableNameComment(split[length - 1]));
        }
    }

    private static String getKeyColumnName(String line) {
        if (StrUtil.isNotBlank(line)) {
            if (line.contains("PRIMARY KEY")) {
                return CharSequenceUtil.subBetween(line.trim(), "(", ")").replace("`", "");
            }
        }
        return null;
    }

    private static String getTableNameComment(String line) {
        String tableNameComment = "";
        if (StrUtil.isNotBlank(line)) {
            if (line.contains("COMMENT=")) {
                String subAfter = CharSequenceUtil.subAfter(line, "COMMENT=", false);
                if (StrUtil.isNotBlank(subAfter)) {
                    tableNameComment = subAfter.trim().replace("'", "");
                }
            }
        }
        return tableNameComment;
    }

    private static String getColumnDefaultValue(String line) {
        if (StrUtil.isNotBlank(line)) {
            int index1 = line.indexOf("DEFAULT");
            if (index1 != -1) {
                return CharSequenceUtil.subBetween(line, "DEFAULT", "COMMENT").trim().replace("'", "");
            }
        }
        return null;
    }

    private static Boolean getColumnIsAuto(String line) {
        return StrUtil.isNotBlank(line) && line.contains("AUTO_INCREMENT");
    }

    private static Integer getColumnTypeLength(String line) {
        if (StrUtil.isNotBlank(line)) {
            String[] split = line.split(" ");
            if (StrUtil.isNotBlank(split[1])) {
                String trim = split[1].trim();
                if (trim.contains("(")) {
                    String subBetween = CharSequenceUtil.subBetween(trim, "(", ")");
                    if (NumberUtil.isNumber(subBetween)) {
                        return Integer.valueOf(subBetween);
                    }
                }
            }
        }
        return null;
    }

    private static Boolean getColumnIsNull(String line) {
        if (StrUtil.isNotBlank(line)) {
            String[] split = line.split(" ");
            if (StrUtil.isNotBlank(split[2])) {
                String trim = split[2].trim();
                if ("NOT".equalsIgnoreCase(trim)) {
                    return false;
                } else if ("UNSIGNED".equalsIgnoreCase(trim)) {
                    return !"NOT".equalsIgnoreCase(split[3].trim());
                }
                return true;
            }
        }
        return null;
    }

    private static String getColumnTypeName(String line) {
        if (StrUtil.isNotBlank(line)) {
            String[] split = line.split(" ");
            if (StrUtil.isNotBlank(split[1])) {
                String trim = split[1].trim();
                if (trim.contains("(")) {
                    return CharSequenceUtil.subBefore(trim, "(", false);
                } else {
                    return trim;
                }
            }
        }
        return null;
    }

    private static String getColumnName(String line) {
        if (StrUtil.isNotBlank(line)) {
            int index = line.indexOf(" ");
            if (index != -1 && !line.contains("PRIMARY KEY")) {
                String substring = line.substring(0, index - 1);
                if (StrUtil.isNotBlank(substring)) {
                    return substring.trim().replace("`", "");
                }
            }
        }
        return null;
    }

    private static String getColumnComment(String line) {
        if (StrUtil.isNotBlank(line)) {
            int index = line.indexOf("COMMENT");
            if (index != -1) {
                String subBetween = CharSequenceUtil.subBetween(line.substring(index + 1), "'", "'");
                if (StrUtil.isNotBlank(subBetween)) {
                    return subBetween.trim().replace("'", "");
                }
            }
        }
        return null;
    }


    private static String getTableName(String line) {
        if (StrUtil.isNotBlank(line)) {
            if (line.contains("CREATE TABLE")) {
                String subBetween = CharSequenceUtil.subBetween(line.trim(), "CREATE TABLE", "(");
                if (StrUtil.isNotBlank(subBetween)) {
                    return subBetween.trim().replace("`", "");
                }
            }
        }
        return null;
    }

}
