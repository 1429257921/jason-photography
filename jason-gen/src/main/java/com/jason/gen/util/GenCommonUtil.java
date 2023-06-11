package com.jason.gen.util;

import cn.hutool.core.text.NamingCase;
import com.jason.gen.constant.GenConstant;

import java.util.Arrays;

/**
 * 代码生成工具类
 *
 * @author guozhongcheng
 * @since 2023/6/10
 **/
public class GenCommonUtil {

    /**
     * 解析表列注释内容，判断是否是枚举字段
     *
     * @param remarks 注释
     * @return 是否枚举字段
     */
    public static boolean isEnumField(String remarks) {
        if (remarks.contains("状态") || remarks.contains("类型")||remarks.contains("删除标志")) {
            return remarks.contains("（") && remarks.contains("）");
        }
        return false;
    }

    /**
     * 获取枚举文件名称
     *
     * @param tableName  表名
     * @param columnName 列名
     * @return 枚举文件名称
     */
    public static String getEnumFileName(String tableName, String columnName) {
        String[] split = tableName.split("_");
        String beginWord = split[0];
        // 过滤表名称前缀
        if (Arrays.asList(GenConstant.FILTER_TABLE_PREFIX).contains(beginWord)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                if (i == 0) {
                    continue;
                }
                sb.append(split[i]);
                if (i == split.length - 1) {
                    continue;
                }
                sb.append("_");
            }
            tableName = sb.toString();
        }
        // 将_转换成大驼峰
        String tableNamePascalCase = NamingCase.toPascalCase(tableName);
        String columnNamePascalCase = NamingCase.toPascalCase(columnName);
        return tableNamePascalCase + columnNamePascalCase + "Enum";
    }
}
