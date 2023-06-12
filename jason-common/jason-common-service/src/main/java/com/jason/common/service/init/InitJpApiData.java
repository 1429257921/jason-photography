package com.jason.common.service.init;

import cn.hutool.core.collection.CollUtil;
import com.google.gson.Gson;
import com.jason.common.service.anno.JpApi;
import com.jason.common.service.context.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 初始化日值注解数据
 *
 * @author guozhongcheng
 * @since 2023/6/12
 */
@Slf4j
public record InitJpApiData(RequestMappingHandlerMapping requestMappingHandlerMapping) implements CommandLineRunner {

    private static final ConcurrentMap<String, String> STATIC_MAP = new ConcurrentHashMap<>(256);

    @Override
    public void run(String... args) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap =
                requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            assert infoEntry.getKey().getPathPatternsCondition() != null;
            Set<PathPattern> patterns = infoEntry.getKey().getPathPatternsCondition().getPatterns();
            if (CollUtil.isNotEmpty(patterns)) {
                for (PathPattern pattern : patterns) {
                    if (pattern != null) {
                        this.buildJpApi(handlerMethod, pattern.getPatternString());
                    }
                }
            }
        }
        System.out.println(new Gson().toJson(STATIC_MAP));
    }

    /**
     * 构建接口和接口名称映射
     */
    private void buildJpApi(HandlerMethod handlerMethod, String url) {
        JpApi jpApi = handlerMethod.getMethodAnnotation(JpApi.class);
        if (jpApi != null) {
            String apiMsg = "[" + jpApi.value() + "]接口";
            STATIC_MAP.put(url, apiMsg);
        }
    }

    /**
     * 获取接口中文描述
     *
     * @param url 示例 /api/auth/login
     * @return 接口中文描述
     */
    public static String getJpApi(String url) {
        return STATIC_MAP.get(url);
    }

    /**
     * 获取接口中文描述
     *
     * @return 接口中文描述
     */
    public static String getJpApi() {
        return STATIC_MAP.get(ContextUtil.getRequest().getServletPath());
    }
}
