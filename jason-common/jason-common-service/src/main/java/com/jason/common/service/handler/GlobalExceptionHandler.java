package com.jason.common.service.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.json.JSONException;
import com.fasterxml.jackson.core.JacksonException;
import com.google.gson.JsonParseException;
import com.jason.common.service.context.JpContextUtil;
import com.jason.common.service.enums.ApiErrorEnum;
import com.jason.common.service.exception.BusinessException;
import com.jason.common.service.vo.JpResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketException;
import java.sql.SQLRecoverableException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author gzc
 * @since 2023/6/12
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = BusinessException.class)
    public Object businessExceptionHandler(HttpServletRequest request, BusinessException e) {
        String url = request.getServletPath();
        log.error("{}发生业务异常, 接口路径{}, 异常原因:{}",
                JpContextUtil.getApiMsg(), url, e);
        log.info("{}发生业务异常, 接口路径{}, 异常原因简单描述为:{}\n",
                JpContextUtil.getApiMsg(), url, e.getErrMsg());
        log.error(String.format("%s:%s", Thread.currentThread().getStackTrace()[1].getMethodName(), e.getErrCode()));
        JpResult<String> result = JpResult.errDetail(e);
        return returnAfter(request, result);
    }

    /**
     * 接口入参参数校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Object methodArgumentNotValidExceptionHandler(HttpServletRequest request,
                                                         MethodArgumentNotValidException e) {
        List<String> violations = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).
                collect(Collectors.toList());
        String errMsg = CollUtil.join(violations, String.valueOf(CharUtil.COMMA));
        String url = request.getServletPath();
        log.error("{}发生参数校验异常, 接口路径{}, 异常原因:{}",
                JpContextUtil.getApiMsg(), url, e);
        log.info("{}发生参数校验异常, 接口路径{}, 异常原因简单描述为:{}\n",
                JpContextUtil.getApiMsg(), url, errMsg);
        JpResult<String> result = JpResult.err(errMsg);
        return returnAfter(request, result);
    }

    /**
     * 处理sql异常
     */
    @ExceptionHandler({SQLRecoverableException.class})
    public Object sqlRecoverableExceptionHandler(HttpServletRequest request, SQLRecoverableException e) {
        String url = request.getServletPath();
        log.error("{}发生sql执行异常, 接口路径{}, 异常原因:{}",
                JpContextUtil.getApiMsg(), url, e);
        log.info("{}发生sql执行异常, 接口路径{}, 异常简单描述为:{}\n",
                JpContextUtil.getApiMsg(), url, e.getMessage());
        JpResult<String> result = JpResult.errDetailMsg(ApiErrorEnum.SQL_EXECUTE_ERROR, e);
        return returnAfter(request, result);
    }

    /**
     * 处理JSON异常
     */
    @ExceptionHandler({JSONException.class, JsonParseException.class, JacksonException.class})
    public Object jsonExceptionExceptionHandler(HttpServletRequest request, Exception e) {
        String url = request.getServletPath();
        log.error("{}发生JSON异常, 接口路径{}, 异常原因:{}",
                JpContextUtil.getApiMsg(), url, e);
        log.info("{}发生JSON异常, 接口路径{}, 异常简单描述为:{}\n",
                JpContextUtil.getApiMsg(), url, e.getMessage());
        JpResult<String> result = JpResult.errDetailMsg(ApiErrorEnum.JSON_CONVERT_ERROR, e);
        return returnAfter(request, result);
    }

    /**
     * 处理网络异常
     */
    @ExceptionHandler(value = {SocketException.class, TimeoutException.class})
    public Object netErrHandler(HttpServletRequest request, Exception e) {
        String url = request.getServletPath();
        log.error("{}发生网络异常, 接口路径{}, 异常原因:{}",
                JpContextUtil.getApiMsg(), url, e);
        log.info("{}发生网络异常, 接口路径{}, 异常简单描述为:{}\n",
                JpContextUtil.getApiMsg(), url, e.getMessage());
        JpResult<String> result = JpResult.errDetailMsg(ApiErrorEnum.NETWORK_ERROR, e);
        return returnAfter(request, result);
    }

    /**
     * 处理未知异常
     */
    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception e) {
        String url = request.getServletPath();
        log.error("{}发生未知异常, 接口路径为{}, 异常原因:{}",
                JpContextUtil.getApiMsg(), url, e);
        log.info("{}发生未知异常, 接口路径为{}, 异常简单描述为:{}\n",
                JpContextUtil.getApiMsg(), url, e.getMessage());
        JpResult<String> result = JpResult.errDetailMsg(ApiErrorEnum.INTERNAL_SERVER_ERROR, e);
        return returnAfter(request, result);
    }


    /**
     * 返回前的操作方法
     */
    protected Object returnAfter(HttpServletRequest request, Object result) {
        return result;
    }
}
