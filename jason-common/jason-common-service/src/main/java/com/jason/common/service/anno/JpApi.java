package com.jason.common.service.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口描述相关注解
 *
 * @author guozhongcheng
 * @since 2023/6/12
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JpApi {

    /**
     * 当前接口中文描述
     */
    String value();

    /**
     * 是否打印当前接口入参数据（打印入参并校验是否为空，为空则抛出异常）
     */
    boolean printReqParam() default true;

    /**
     * 当前接口是否请求响应结果日志入库
     */
    boolean reqLogInsertDB() default true;

}
