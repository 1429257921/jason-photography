package com.jason.gen.v2;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;

import java.lang.reflect.Field;

/**
 * TODO
 *
 * @author guozhongcheng
 * @since 2023/6/16
 **/
public class JpValidationUtil {

    public static void check(Object obj) throws Exception {
        if (ObjUtil.isNotEmpty(obj)) {
            Field[] fieldArr = obj.getClass().getDeclaredFields();
            if (ArrayUtil.isNotEmpty(fieldArr)) {
                for (Field field : fieldArr) {
                    field.setAccessible(true);
                    JpNotNull annotation = field.getAnnotation(JpNotNull.class);
                    if (annotation != null) {
                        String message = annotation.message();
                        Object value = field.get(obj);
                        if (ObjUtil.isEmpty(value)) {
                            throw new Exception(message);
                        }
                    }
                }
            }
        }
    }
}
