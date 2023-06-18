package com.jason.gen.util;

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
    @SuppressWarnings("all")
    public static boolean isEnumField(String remarks) {
        if (remarks.contains("状态") || remarks.contains("类型") || remarks.contains("删除标志")) {
            return remarks.contains("（") && remarks.contains("）");
        }
        return false;
    }


}
