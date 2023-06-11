package com.jason.common.service.filter;

import cn.hutool.core.util.StrUtil;
import com.jason.common.service.wrapper.MyRequestWrapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 自定义过滤器
 *
 * @author gzc
 * @since 2023/6/12
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalHandlerFilter implements Filter {

    public static final String MULTIPART = "multipart/";

    /**
     * 将请求放入请求链中
     * 创建一个入口，在这个入口中定义一个机会：
     * 将我们自定义的RSAHttpServletRequestWrapper代替HttpServletRequest随着请求传递下去
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            String contentType = req.getContentType();
            //文件上传类型
            if (StrUtil.isNotBlank(contentType) && contentType.startsWith(MULTIPART)) {
                chain.doFilter(request, response);
                return;
            }

            MyRequestWrapper rw = new MyRequestWrapper(req);
            chain.doFilter(rw, response);
        } catch (Exception e) {
            log.error("将请求放入请求链中异常->", e);
        }
    }
}
