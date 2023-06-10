package com.jason.gen.entity;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 数据库列信息
 *
 * @author guozhongcheng
 * @see TableField.MetaInfo 属性描述参数该类
 * @since 2023/6/10
 **/
@Data
public class ColumnInfo implements Serializable {
    private String tableName;
    private String columnName;
    private String remarks;
    private Integer length;
    private Integer scale;
    private String defaultValue;
    private String jdbcType;
    private Boolean nullable;

    /**
     * 构建表字段信息对象
     */
    public static ColumnInfo build(TableField.MetaInfo metaInfo) {
        JSONObject jsonObject = new JSONObject();
        String metaInfoStr = metaInfo.toString();
        // MetaInfo{tableName=k_user_login_record, columnName=login_status, length=3, nullable=false,
        // remarks='操作类型（0、登录成功，1、登出成功，2、登录失败，3、登出失败）', defaultValue='null', scale=0, jdbcType=TINYINT}
        int indexBegin = metaInfoStr.indexOf("{");
        int indexEnd = metaInfoStr.lastIndexOf("}");
        String substring = metaInfoStr.substring(indexBegin + 1, indexEnd);
        String[] split = substring.split(",");
        for (String item : split) {
            String sss = item.trim();
            String[] split1 = sss.split("=");
            String key = split1[0];
            String value = split1[1];
            // 去除''符号
            if (value.contains("null")) {
                value = "null";
            }
            // 去除''符号和空格
            if ("remarks".equals(key)) {
                value = value.substring(1, value.length() - 1).trim();
            }
            jsonObject.set(key, value);
        }
        return JSONUtil.toBean(jsonObject, ColumnInfo.class);
    }
}
