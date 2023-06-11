package com.jason.common.service.context;

import com.jason.common.service.exception.Assert;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * spring上下文工具类
 *
 * @author gzc
 * @since 2023/6/12
 **/
@SuppressWarnings("unused")
public class ContextUtil {

    /**
     * 获取当前的request
     * 这里如果报空指针异常是因为单独使用spring获取request
     * 需要在配置文件里添加监听
     */
    public static HttpServletRequest getRequest() {
        return Assert.notNull(getRequestAttributes().getRequest(), "request can not null");
    }

    /**
     * 获取当前响应对象response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session会话
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 核心类RequestContextHolder
     */
    public static ServletRequestAttributes getRequestAttributes() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    }

    /**
     * 获取服务上下文
     */
    public static ServletContext getServletContext() {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        return webApplicationContext != null ? webApplicationContext.getServletContext() : null;
    }

    /**
     * 获取请求相对路径(不包含ip、端口和上下文)
     */
    public static String getServletPath() {
        return getRequest().getServletPath();
    }

    /**
     * 获取请求完整的路径（不包含ip和端口）
     */
    public static String getIntactPath() {
        return getRequest().getContextPath() + getServletPath();
    }

}
