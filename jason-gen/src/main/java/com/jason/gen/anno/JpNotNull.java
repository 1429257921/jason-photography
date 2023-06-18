package com.jason.gen.anno;

import com.jason.gen.util.JpValidationUtil;

import java.lang.annotation.*;

/**
 * 添加了该注解的属性，使用{@link JpValidationUtil#check(Object)} 工具类去校验对象，
 * 如果有该注解的属性为空则抛出异常
 *
 * @author guozhongcheng
 * @since 2023/6/16
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JpNotNull {
    /**
     * 错误信息
     */
    String message();
}
