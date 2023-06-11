package com.jason.common.service.context;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jason.common.service.exception.BusinessException;
import com.jason.common.service.wrapper.MyRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义spring上下文工具类
 *
 * @author gzc
 * @since 2023/6/12
 **/
@Slf4j
@SuppressWarnings("unused")
public class JpContextUtil extends ContextUtil {

    /**
     * 获取当前request的包装类(可获取请求body中的参数)
     */
    public static MyRequestWrapper getRequestWrapper() {
        return new MyRequestWrapper(getRequestAttributes().getRequest());
    }

    /**
     * 通过在接口上添加APIMessage注解，获取接口中文描述
     */
    public static String getApiMsg() {
        return "";
    }

    public static String getToken() {
        return "";
    }


    /**
     * 获取请求参数(可获取URL或body中的参数,如果URL中有参数则不获取body中的参数)
     */
    public static String getReqParam() {
        StringBuilder sb = new StringBuilder();
        String urlParam = getUrlParam();
        if (StrUtil.isNotBlank(urlParam)) {
            sb.append("请求URL中的参数->");
            sb.append(urlParam);
            sb.append("\n");
        }
        String bodyParam = getBodyParam();
        if (StrUtil.isNotBlank(bodyParam)) {
            sb.append("请求body中的参数->");
            sb.append(bodyParam);
        }
        String requestBody = sb.toString();
        //不为空, 移除换行符、空白符合制表符
        if (StrUtil.isNotBlank(requestBody)) {
            requestBody = requestBody.replaceAll("[\\s*\\t\\n\\r]", "");
        }
        return requestBody;
    }

    /**
     * 获取当前请求request中Body请求参数
     */
    public static String getBodyParam() {
        String requestBody;
        try {
            MyRequestWrapper requestWrapper = getRequestWrapper();
            requestBody = requestWrapper.getBody();
            JsonElement jsonElement = JsonParser.parseString(requestBody);
            if (jsonElement.isJsonObject() || jsonElement.isJsonArray()) {
                if (StrUtil.contains(requestBody, CharPool.AMP)) {
                    Map<String, String> map = HttpUtil.decodeParamMap(requestBody, Charset.defaultCharset());
                    requestBody = new Gson().toJson(map);
                }
            }
        } catch (Exception e) {
            throw new BusinessException(getApiMsg() + "获取Body中的请求参数异常:" + e.getMessage(), e);
        }
        return requestBody;
    }

    /**
     * 获取当前请求request中URL请求参数
     */
    public static String getUrlParam() {
        String requestBody;
        try {
            Map<String, String[]> parameterMap = getRequest().getParameterMap();
            requestBody = convert(parameterMap);
        } catch (Exception e) {
            throw new BusinessException(getApiMsg() + "获取URL中的请求参数异常:" + e.getMessage(), e);
        }
        return requestBody;
    }

    /**
     * 获取URL中的参数
     */
    private static String convert(Map<String, String[]> map) {
        String requestBody = "";
        try {
            Map<String, String> hashMap = new HashMap<>(16);
            // 取value数组中的第一个元素
            map.forEach((k, v) -> hashMap.put(k, ArrayUtil.isNotEmpty(v) ? v[0] : ""));
            if (CollUtil.isNotEmpty(hashMap)) {
                Gson gson = new Gson();
                requestBody = gson.toJson(hashMap);
            }
        } catch (Exception e) {
            log.error("URL中的请求参数获取并转map异常->", e);
        }
        return requestBody;
    }

}
