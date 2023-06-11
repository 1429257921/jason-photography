package com.jason.gen.entity;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.jason.gen.constant.GenConstant;
import com.jason.gen.util.GenCommonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库列描述枚举信息
 *
 * @author guozhongcheng
 * @since 2023/6/10
 **/
@Data
@Slf4j
public class RemarksEnumInfo implements Serializable {
    private String enumPackage;
    private String tableName;
    private String comment;
    private String commentDesc;
    private String author;
    private String date;
    /**
     * 枚举文件名称（表名大驼峰+字段名大驼峰）
     */
    private String enumFileName;
    private List<EnumValue> enumValueList;

    public static RemarksEnumInfo build(ColumnInfo columnInfo, GlobalConfig globalConfig) {
        RemarksEnumInfo remarksEnumInfo = new RemarksEnumInfo();
        remarksEnumInfo.setAuthor(globalConfig.getAuthor());
        remarksEnumInfo.setDate(LocalDate.now().toString());
        remarksEnumInfo.setTableName(columnInfo.getTableName());
        remarksEnumInfo.setEnumPackage(GenConstant.JASON_PHOTOGRAPHY_DAO_ENUM_PACKAGE);
        remarksEnumInfo.setEnumFileName(GenCommonUtil.getEnumFileName(columnInfo.getTableName(), columnInfo.getColumnName()));
        String remarks = columnInfo.getRemarks();
        if (GenCommonUtil.isEnumField(remarks)) {
            int indexBegin = remarks.indexOf("（");
            int indexEnd = remarks.indexOf("）");
            String comment = remarks.substring(0, indexBegin);
            remarksEnumInfo.setComment(comment);
            remarksEnumInfo.setCommentDesc(remarks.substring(indexBegin, indexEnd + 1));
            String enumValueStr = remarks.substring(indexBegin + 1, indexEnd).trim();
            String[] split1 = enumValueStr.split("，");
            List<EnumValue> enumValueList = new ArrayList<>(8);
            for (String sp1 : split1) {
                EnumValue enumValue = new EnumValue();
                String[] split2 = sp1.split("、");
                enumValue.setValue(Integer.valueOf(split2[0].trim()));
                String desc = split2[1].trim();
                enumValue.setDesc(desc);
                enumValue.setEnumName(PinyinUtil.getFirstLetter(desc, ""));
                enumValueList.add(enumValue);
            }
            remarksEnumInfo.setEnumValueList(enumValueList);
            if ("status".equals(columnInfo.getColumnName()) && "启用状态".equals(comment.trim())) {
                remarksEnumInfo.setEnumFileName(GenCommonUtil.getEnumFileName("", columnInfo.getColumnName()));
            }
            if ("del_flag".equals(columnInfo.getColumnName()) && "删除标志".equals(comment.trim())) {
                remarksEnumInfo.setEnumFileName(GenCommonUtil.getEnumFileName("", columnInfo.getColumnName()));
            }
        }


        return remarksEnumInfo;
    }
}