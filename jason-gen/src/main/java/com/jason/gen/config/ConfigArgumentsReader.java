package com.jason.gen.config;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jason.gen.constant.GenConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * 配置参数读取类，主要实现了对genPhotography.properties文件和type-converter.properties文件的读取操作，
 * 对读取后的properties进行封装
 *
 * @author gzc
 * @since 2023/6/12 23:46
 **/
public abstract class ConfigArgumentsReader {

    private static final Properties GEN_ARGS_PROPERTIES = new Properties(16);
    private static final Properties TYPE_CONVERT_PROPERTIES = new Properties(16);

    static {
        try {
            initProperties(GEN_ARGS_PROPERTIES, "genPhotography.properties");
            initProperties(TYPE_CONVERT_PROPERTIES, "type-converter.properties");

            GenArgs genArgs = parseProperties(GEN_ARGS_PROPERTIES, GenArgs.class);
            parseProperties(TYPE_CONVERT_PROPERTIES, List.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> T parseProperties(Properties properties, Class<T> cls) {
        JSONObject jsonObject = JSONUtil.createObj();
        Set<String> propertyNameSet = properties.stringPropertyNames();
        for (String propertyName : propertyNameSet) {
            jsonObject.set(propertyName, properties.getProperty(propertyName));
        }
        return jsonObject.toBean(cls);
    }

    private static void initProperties(Properties properties, String propertiesFileName) throws Exception {
        InputStream inputStream = ConfigArgumentsReader.class
                .getClassLoader().getResourceAsStream(propertiesFileName);
        try {
            properties.load(IoUtil.getReader(inputStream, GenConstant.CHARSET));
        } catch (IOException e) {
            throw new Exception("解析properties文件发生异常", e);
        }
    }


    public static class GenArgs {
        public static String projectPath;
        public static String modules;
        public static String author;
        public static String tables;
        public static String databaseUrl;
        public static String databaseUsername;
        public static String databasePassword;
    }


    public static class TypeConverter {
        private String jdbcTypeName;
        private String javaTypeName;
        private String javaTypePackage;
    }

}
