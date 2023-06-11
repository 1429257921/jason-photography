package com.jason.common.http.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.jason.common.core.exception.BizException;
import com.jason.common.core.util.JpIoUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

/**
 * OkHttp工具类
 *
 * @author gzc
 * @since 2023/6/12
 */
@Slf4j
@SuppressWarnings("unused")
public record OkHttpService(OkHttpClient okHttpClient) {

    public static MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    public static MediaType XML_MEDIA_TYPE = MediaType.parse("application/xml; charset=utf-8");
    public static MediaType PNG_MEDIA_TYPE = MediaType.parse("image/png; charset=utf-8");
    public static MediaType PDF_MEDIA_TYPE = MediaType.parse("application/pdf; charset=utf-8");
    public static MediaType FROM_MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded");

    /**
     * get 请求
     *
     * @param url 请求url地址
     * @return string
     */
    public String doGet(String apiMsg, String url) {
        return executeGet(apiMsg, url, null, null);
    }


    /**
     * get 请求
     *
     * @param url      请求url地址
     * @param paramMap 请求参数 map
     * @return string
     */
    public String doGet(String apiMsg, String url, Map<String, String> paramMap) {
        return executeGet(apiMsg, url, paramMap, null);
    }

    /**
     * get 请求
     *
     * @param url 请求url地址
     *            //	 * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doGetHeader(String apiMsg, String url, Map<String, String> headMap) {
        return executeGet(apiMsg, url, null, headMap);
    }


    /**
     * get 请求
     *
     * @param url      请求url地址
     * @param paramMap 请求参数 map
     *                 //	 * @param headers  请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String executeGet(String apiMsg, String url, Map<String, String> paramMap,
                             Map<String, String> headMap) {
        log.info("调用{}, 请求路径为{}, 请求参数为{}", apiMsg, url, new Gson().toJson(paramMap));
        StringBuilder sb;
        Request.Builder builder;
        try {
            Assert.notBlank(url, "请求路径为空");
            sb = new StringBuilder(url);
            if (paramMap != null && paramMap.keySet().size() > 0) {
                boolean firstFlag = true;
                for (String key : paramMap.keySet()) {
                    if (firstFlag) {
                        sb.append("?").append(key).append("=").append(paramMap.get(key));
                        firstFlag = false;
                    } else {
                        sb.append("&").append(key).append("=").append(paramMap.get(key));
                    }
                }
            }

            builder = new Request.Builder();
            if (CollUtil.isNotEmpty(headMap)) {
                log.info("调用{}, 请求头为{}", apiMsg, new Gson().toJson(headMap));
                headMap.forEach(builder::addHeader);
            }

        } catch (Exception e) {
            throw new BizException("调用" + apiMsg + " 拼接GET参数发生异常", e);
        }

        try {
            Request request = builder.url(sb.toString()).build();
            byte[] bytes = executeBytes(request);
            String responseText = StrUtil.str(bytes, CharsetUtil.CHARSET_UTF_8);
            if (StrUtil.isBlank(responseText)) {
                throw new BizException("返回结果为空");
            }
            log.info("调用{}返回结果为{}", apiMsg, responseText);
            return responseText;
        } catch (Exception e) {
            throw new BizException("调用" + apiMsg + "发生异常, 异常原因为" + e.getMessage(), e);
        }
    }

    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url  请求url地址
     * @param json 请求数据, json 字符串
     * @return string
     */
    public String doPostJson(String apiMsg, String url, String json) {
        return executePost(apiMsg, url, json, null, JSON_MEDIA_TYPE);
    }

    public byte[] doPostJson(String url, String json) {
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, json);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return executeBytes(request);
    }

    public byte[] doPostJson(String url, String json, Map<String, String> headMap) {
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, json);
        Request.Builder reqBuilder = new Request.Builder().url(url).post(requestBody);

        if (CollUtil.isNotEmpty(headMap)) {
            headMap.forEach(reqBuilder::addHeader);
        }
        Request request = reqBuilder.build();
        return executeBytes(request);
    }

    /**
     * post 请求, 请求数据为 xml 的字符串
     *
     * @param url 请求url地址
     * @param xml 请求数据, xml 字符串
     * @return string
     */
    public String doPostXml(String apiMsg, String url, String xml) {
        return executePost(apiMsg, url, xml, null, XML_MEDIA_TYPE);
    }


    public String executePost(String apiMsg, String url, String data,
                              Map<String, String> headMap, MediaType contentType) {
        try {
            log.info("调用{}, 请求路径为{}, 请求参数为{}", apiMsg, url, data);
            Assert.notBlank(url, "请求路径为空");
            RequestBody requestBody = RequestBody.create(contentType, data);
            Request.Builder reqBuilder = new Request.Builder().url(url).post(requestBody);
            if (CollUtil.isNotEmpty(headMap)) {
                log.info("调用{}, 请求头为{}", apiMsg, new Gson().toJson(headMap));
                headMap.forEach(reqBuilder::addHeader);
            }
            Request request = reqBuilder.build();
            // 发起请求
            byte[] bytes = executeBytes(request);
            String responseText = StrUtil.str(bytes, CharsetUtil.CHARSET_UTF_8);
            if (StrUtil.isBlank(responseText)) {
                throw new BizException("调用接口返回结果为空");
            }
            log.info("调用{}返回结果为{}", apiMsg, responseText);
            return responseText;
        } catch (Exception e) {
            throw new BizException("调用" + apiMsg + "发生异常, 异常原因为" + e.getMessage(), e);
        }
    }

    /**
     * 发起请求
     */
    public InputStream executeStream(Request request) {
        ByteArrayOutputStream byteArrayOutputStream = executeByteOutStream(request);
        if (byteArrayOutputStream != null) {
            return JpIoUtil.getInputStream(byteArrayOutputStream);
        }
        return null;
    }

    /**
     * 发起请求
     */
    public ByteArrayOutputStream executeByteOutStream(Request request) {
        byte[] bytes = executeBytes(request);
        if (ArrayUtil.isNotEmpty(bytes)) {
            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(bytes);
            return JpIoUtil.cloneInputStream(byteArrayInputStream);
        }
        return null;
    }

    /**
     * 发起请求
     */
    public byte[] executeBytes(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return Objects.requireNonNull(response.body()).bytes();
            }
        } catch (Exception e) {
            throw new BizException(e.getMessage(), e);
        }
        return new byte[0];
    }


}
