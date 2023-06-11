package com.jason.common.thread;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 子父线程变量共享工具类
 *
 * @author gzc
 * @since 2023/6/12
 **/
@SuppressWarnings({"unused", "unchecked"})
public abstract class TransmittableThreadLocalUtil {
    /**
     * 可实现主线程与线程池中的线程之间的值传递（需要引入依赖）
     */
    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL;

    static {
        THREAD_LOCAL = new TransmittableThreadLocal<>();
        THREAD_LOCAL.set(new HashMap<>(5));
    }

    public static Map<String, Object> getThreadLocal() {
        return THREAD_LOCAL.get();
    }

    public static <T> T get(String key) {
        return get(key, null);
    }

    public static <T> T get(String key, T defaultValue) {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new HashMap<>(1);
        }
        return (T) Optional.ofNullable(map.get(key)).orElse(defaultValue);
    }

    public static void set(String key, Object value) {
        Map<String, Object> map = Optional.ofNullable(THREAD_LOCAL.get()).orElse(new HashMap<>(10));
        map.put(key, value);
    }

    public static void set(Map<String, Object> keyValueMap) {
        Map<String, Object> map = Optional.ofNullable(THREAD_LOCAL.get()).orElse(new HashMap<>(10));
        map.putAll(keyValueMap);

    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static <T> T remove(String key) {
        Map<String, Object> map = THREAD_LOCAL.get();
        return (T) map.remove(key);
    }

}
