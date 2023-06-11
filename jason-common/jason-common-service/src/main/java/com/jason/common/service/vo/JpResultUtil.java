package com.jason.common.service.vo;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.jason.common.service.enums.ApiErrorEnum;
import com.jason.common.service.exception.BusinessException;

/**
 * 响应VO工具类
 *
 * @author gzc
 * @since 2023/6/12
 */
@SuppressWarnings("unused")
public class JpResultUtil {

    public static JpResult<String> ok() {
        return new JpResult<>(ApiErrorEnum.SUCCESS.getCode(), ApiErrorEnum.SUCCESS.getMsg(), "");
    }

    public static <T> JpResult<T> ok(T data) {
        return new JpResult<>(ApiErrorEnum.SUCCESS.getCode(), ApiErrorEnum.SUCCESS.getMsg(), data);
    }

    public static JpResult<String> okMsg(String okMsg) {
        return new JpResult<>(ApiErrorEnum.SUCCESS.getCode(), okMsg, "");
    }

    public static JpResult<String> err(ApiErrorEnum errorEnum) {
        return new JpResult<>(errorEnum.getCode(), errorEnum.getMsg(), "");
    }

    public static JpResult<String> errDetailMsg(ApiErrorEnum errorEnum, String data) {
        return new JpResult<>(errorEnum.getCode(), errorEnum.getMsg(), data);
    }

    public static JpResult<String> errDetailMsg(ApiErrorEnum errorEnum, Exception e) {
        return new JpResult<>(errorEnum.getCode(), errorEnum.getMsg(), ExceptionUtil.stacktraceToString(e));
    }

    public static JpResult<String> err(String errMsg) {
        return new JpResult<>(ApiErrorEnum.ERROR.getCode(), errMsg, "");
    }

    public static JpResult<String> err(String errMsg, String errDetailMsg) {
        return new JpResult<>(ApiErrorEnum.ERROR.getCode(), errMsg, errDetailMsg);
    }

    public static JpResult<String> err(BusinessException e) {
        return new JpResult<>(e.getErrCode(), e.getErrMsg(), "");
    }

    public static JpResult<String> errDetail(BusinessException e) {
        return new JpResult<>(e.getErrCode(), e.getErrMsg(), ExceptionUtil.stacktraceToString(e));
    }
}
