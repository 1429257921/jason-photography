package com.jason.gen.util;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import com.jason.gen.anno.JpNotNull;

import java.lang.reflect.Field;

/**
 * 校验参数空值工具类
 *
 * @author guozhongcheng
 * @since 2023/6/16
 **/
public class JpValidationUtil {

    /**
     * 校验参数
     *
     * @param obj 需要校验的对象
     * @throws Exception 抛出空属性的错误信息
     */
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
                        if (value.getClass().getPackageName().contains("com.jason.gen")) {
                            // 递归
                            check(value);
                        }
                    }
                }
            }
        }
    }
}
