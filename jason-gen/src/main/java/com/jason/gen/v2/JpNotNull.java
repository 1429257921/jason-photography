package com.jason.gen.v2;

import java.lang.annotation.*;

/**
 * TODO
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
