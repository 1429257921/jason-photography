package com.jason.common.service.wrapper;

import cn.hutool.core.io.IoUtil;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 因为HttpServletRequest对象的body数据只能get，不能set，即不能再次赋值。
 * 而我们的需求是需要给HttpServletRequest赋值，
 * 所以需要定义一个HttpServletRequest实现类：MyRequestWrapper，
 * 这个实现类可以被赋值来满足我们的需求。
 *
 * @author gzc
 * @since 2023/6/12
 */
@Slf4j
@SuppressWarnings("unused")
public class MyRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 保存request body的数据
     */
    private String body;

    /**
     * 保存请求头信息
     */
    private final Map<String, String> headerMap = new HashMap<>(16);

    /**
     * 解析request的inputStream(即body)数据，转成字符串
     */
    public MyRequestWrapper(HttpServletRequest request) {
        super(request);
        try {
            body = IoUtil.readUtf8(request.getInputStream());
        } catch (Exception e) {
            log.error("{}创建请求包装器发生异常->{}", request.getServletPath(), e);
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() {
                return inputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if (headerMap.containsKey(name)) {
            headerValue = headerMap.get(name);
        }
        return headerValue;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> headerNameList = Collections.list(super.getHeaderNames());
        headerNameList.addAll(headerMap.keySet());
        return Collections.enumeration(headerNameList);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> headerValueList = Collections.list(super.getHeaders(name));
        if (headerMap.containsKey(name)) {
            headerValueList = List.of(headerMap.get(name));
        }
        return Collections.enumeration(headerValueList);
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}
