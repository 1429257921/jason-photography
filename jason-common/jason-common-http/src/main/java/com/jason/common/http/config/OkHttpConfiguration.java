package com.jason.common.http.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp配置类
 *
 * @author gzc
 * @since 2023/6/12
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("all")
public record OkHttpConfiguration(ConfigurableEnvironment configurableEnvironment) {

    /**
     * 连接超时时间(秒)
     */
    private static long connectTimeout = 60;
    /**
     * 读取超时时间(秒)
     */
    private static long readTimeout = 60;
    /**
     * 写入超时时间(秒)
     */
    private static long writeTimeout = 60;
    /**
     * 连接池中整体的空闲连接的最大数量
     */
    private static int maxIdleConnections = 100;
    /**
     * 连接空闲时间最多为 300 秒
     */
    private static long keepaliveDuration = 250L;

    @Bean
    public OkHttpClient okHttpClient() {
        connectTimeout = Long.parseLong(Objects.requireNonNull(configurableEnvironment.getProperty("ok.http.connect-timeout")));
        readTimeout = Long.parseLong(Objects.requireNonNull(configurableEnvironment.getProperty("ok.http.read-timeout")));
        writeTimeout = Long.parseLong(Objects.requireNonNull(configurableEnvironment.getProperty("ok.http.write-timeout")));
        log.info("okHttpConfiguration配置信息connectTimeout->{},readTimeout->{},writeTimeout->{}",
                connectTimeout, readTimeout, writeTimeout);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                // 是否开启缓存
                .retryOnConnectionFailure(false)
                .connectionPool(pool())
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true)
                // 设置代理
//            	.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)))
                // 拦截器
//                .addInterceptor()
                .build();
        return okHttpClient;
    }

    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @Bean
    public SSLSocketFactory sslSocketFactory() {
        try {
            // 信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error("创建sslSocketFactory发生密钥异常, 异常原因->", e);
        }
        return null;
    }

    @Bean
    public ConnectionPool pool() {
        maxIdleConnections = Integer.parseInt(Objects.requireNonNull(configurableEnvironment.getProperty("ok.http.max-idle-connections")));
        keepaliveDuration = Long.parseLong(Objects.requireNonNull(configurableEnvironment.getProperty("ok.http.keep-alive-duration")));
        return new ConnectionPool(
                maxIdleConnections,
                keepaliveDuration,
                TimeUnit.SECONDS);
    }
}
