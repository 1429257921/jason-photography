//package com.jason.gen.config;
//
//import cn.hutool.core.io.IoUtil;
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.generator.config.GlobalConfig;
//import com.baomidou.mybatisplus.generator.config.po.TableField;
//import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
//import com.baomidou.mybatisplus.generator.type.ITypeConvertHandler;
//import com.baomidou.mybatisplus.generator.type.TypeRegistry;
//import com.jason.gen.constant.GenConstant;
//import com.jason.gen.entity.ColumnInfo;
//import com.jason.gen.util.GenCommonUtil;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.Charset;
//import java.util.Properties;
//
///**
// * TODO
// *
// * @author guozhongcheng
// * @since 2023/6/9
// **/
//@SuppressWarnings("ALL")
//public class FieldTypeConvertHandler implements ITypeConvertHandler {
//
//    private static final Properties PROPERTIES = new Properties();
//
//    static {
//        InputStream inputStream = FieldTypeConvertHandler.class
//                .getClassLoader().getResourceAsStream("type-converter.properties");
//        try {
//            PROPERTIES.load(IoUtil.getReader(inputStream, Charset.defaultCharset()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public IColumnType convert(GlobalConfig globalConfig, TypeRegistry typeRegistry, TableField.MetaInfo metaInfo) {
//        String fieldType = metaInfo.getJdbcType().name();
//        String convertTypeName = null;
//        // 第一次全字符匹配
//        for (String item : PROPERTIES.stringPropertyNames()) {
//            if (fieldType.toLowerCase().equalsIgnoreCase(item)) {
//                convertTypeName = PROPERTIES.getProperty(item);
//                break;
//            }
//        }
//        //第一次全字符匹配未匹配上时，进行第二次模糊匹配
//        if (StrUtil.isBlank(convertTypeName)) {
//            for (String item : PROPERTIES.stringPropertyNames()) {
//                if (fieldType.toLowerCase().contains(item)) {
//                    convertTypeName = PROPERTIES.getProperty(item);
//                    break;
//                }
//            }
//        }
//        // 判断是否是枚举字段
//        if (GenCommonUtil.isEnumField(metaInfo.getRemarks())) {
//            return new IColumnType() {
//                @Override
//                public String getType() {
//                    ColumnInfo columnInfo = ColumnInfo.build(metaInfo);
//                    // 你想修改的类型
//                    return GenCommonUtil.getEnumFileName(columnInfo.getTableName(), columnInfo.getColumnName());
//                }
//
//                @Override
//                public String getPkg() {
//                    // 类型所在包
//                    return GenConstant.JASON_PHOTOGRAPHY_DAO_ENUM_PACKAGE;
//                }
//            };
//        }
//
////        System.out.println("mysql类型->" + fieldType + "，java类型->" + convertTypeName);
//        if (StrUtil.isNotBlank(convertTypeName)) {
//            String finalConvertTypeName = convertTypeName;
//            return new IColumnType() {
//                @Override
//                public String getType() {
//                    // 你想修改的类型
//                    return finalConvertTypeName;
//                }
//
//                @Override
//                public String getPkg() {
//                    // 类型所在包
//                    return null;
//                }
//            };
//        }
//
//        return typeRegistry.getColumnType(metaInfo);
//    }
//}
