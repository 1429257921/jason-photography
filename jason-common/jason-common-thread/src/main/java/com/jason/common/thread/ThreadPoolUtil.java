package com.jason.common.thread;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池工具类
 *
 * @author gzc
 * @since 2023/6/12
 */
@Slf4j
@SuppressWarnings("unused")
public abstract class ThreadPoolUtil {

    /**
     * IO密集型线程名称前缀
     */
    private static final String POOL_THREAD_PREFIX = "pool-";
    /**
     * CPU密集型线程名称前缀
     */
    private static final String FAST_POOL_THREAD_PREFIX = "fastPool-";
    /**
     * 阻塞型线程名称前缀
     */
    private static final String BLOCK_POOL_THREAD_PREFIX = "blockPool-";
    /**
     * IO密集型线程池，如http请求，文件输入输出等
     */
    private static ThreadPoolExecutor pool;
    /**
     * CPU密集型线程池，如数据解析、校验、计算等
     */
    private static ThreadPoolExecutor fastPool;
    /**
     * 阻塞型线程池，小型池，适用短时间的并发执行，主线程阻塞等待结果
     */
    private static ThreadPoolExecutor blockPool;
    /**
     * 1+目标IO耗时/目标CPU耗时
     */
    private static final int FACTOR = 1 + 2000 / 20;
    /**
     * 核心数
     */
    private static final int CPU_CORE = Runtime.getRuntime().availableProcessors();
    /**
     * IO密集型线程池核心数大小
     */
    private static final int POOL_CORE_SIZE = CPU_CORE * FACTOR;
    /**
     * IO密集型线程池线程最大数量
     */
    private static final int POOL_MAX_SIZE = POOL_CORE_SIZE * 2;
    /**
     * CPU密集型线程池核心数大小
     */
    private static final int FAST_POOL_CORE_SIZE = CPU_CORE;
    /**
     * CPU密集型线程池线程最大数量
     */
    private static final int FAST_POOL_MAX_SIZE = FAST_POOL_CORE_SIZE * 2;
    /**
     * 存活时间 30纳秒
     */
    private static final long KEEP_ALIVE_TIME = TimeUnit.SECONDS.toNanos(30L);
    /**
     * 存活时间类型(纳秒)
     */
    private static final TimeUnit KEEP_ALIVE_TIME_TIME_UNIT = TimeUnit.NANOSECONDS;
    /**
     * 容纳能力
     */
    private static final int CAPACITY = 10000;
    /**
     * IO密集型线程池队列大小
     */
    private static final int POOL_QUEUE_SIZE = CAPACITY * 2;
    /**
     * CPU密集型线程池队列大小
     */
    private static final int FAST_POOL_QUEUE_SIZE = CAPACITY;
    /**
     * IO密集型线程池队列 (无界的任务队列)
     */
    private static final BlockingQueue<Runnable> POOL_QUEUE = new LinkedBlockingQueue<>(POOL_QUEUE_SIZE);
    /**
     * CPU密集型线程池队列 (无界的任务队列)
     */
    private static final BlockingQueue<Runnable> FAST_POOL_QUEUE = new LinkedBlockingQueue<>(FAST_POOL_QUEUE_SIZE);

    static {
        init();
    }

    /**
     * 初始化线程池
     * <p>
     * ThreadPoolExecutor对象构造方法参数详解
     * corePoolSize :指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去。
     * maximumPoolSize: 指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量。
     * keepAliveTime: 当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁。
     * unit: keepAliveTime的单位。
     * workQueue: 任务队列，被添加到线程池中，但尚未被执行的任务；它一般分为直接提交队列、有界任务队列、无界任务队列、优先任务队列几种。
     * threadFactory: 线程工厂，用于创建线程，一般用默认即可。
     * handler: 拒绝策略；当任务太多来不及处理时，如何拒绝任务。
     */
    private static synchronized void init() {
        shutdownNow(pool, fastPool, blockPool);
        log.info("开始初始化线程池！！！");

        ThreadPoolExecutor srcPool =
                new ThreadPoolExecutor(
                        POOL_CORE_SIZE,
                        POOL_MAX_SIZE,
                        KEEP_ALIVE_TIME,
                        KEEP_ALIVE_TIME_TIME_UNIT,
                        POOL_QUEUE,
                        new NamedThreadFactory(POOL_THREAD_PREFIX),
                        // 满队列线程则由主线程自行执行
                        new ThreadPoolExecutor.CallerRunsPolicy());


        ThreadPoolExecutor srcFastPool =
                new ThreadPoolExecutor(
                        FAST_POOL_CORE_SIZE,
                        FAST_POOL_MAX_SIZE,
                        KEEP_ALIVE_TIME,
                        KEEP_ALIVE_TIME_TIME_UNIT,
                        FAST_POOL_QUEUE,
                        new NamedThreadFactory(FAST_POOL_THREAD_PREFIX),
                        // 满队列线程则由主线程自行执行
                        new ThreadPoolExecutor.CallerRunsPolicy());

        ThreadPoolExecutor srcBlockPool =
                new ThreadPoolExecutor(
                        CPU_CORE,
                        CPU_CORE,
                        KEEP_ALIVE_TIME,
                        KEEP_ALIVE_TIME_TIME_UNIT,
                        new SynchronousQueue<>(),
                        new NamedThreadFactory(BLOCK_POOL_THREAD_PREFIX),
                        // 满队列线程则由主线程自行执行
                        new ThreadPoolExecutor.CallerRunsPolicy());

        pool = srcPool;
        fastPool = srcFastPool;
        blockPool = srcBlockPool;
        log.info("初始化线程池成功！！！");

    }

    /**
     * 销毁线程池
     */
    private static void shutdownNow(ThreadPoolExecutor... pools) {
        log.info("开始销毁线程池！！！");
        for (ThreadPoolExecutor pool : pools) {
            if (pool != null) {
                pool.shutdownNow();
            }
        }
        log.info("销毁线程池成功！！！");
    }

    /**
     * 获取IO密集型线程池
     */
    public static ThreadPoolExecutor pool() {
        return pool;
    }

    /**
     * 获取CPU密集型线程池
     */
    public static ThreadPoolExecutor fastPool() {
        return fastPool;
    }

    /**
     * 获取阻塞型线程池
     */
    public static ThreadPoolExecutor blockPool() {
        return blockPool;
    }


    /**
     * 自定义线程工厂
     */
    static class NamedThreadFactory implements ThreadFactory {
        /**
         * 线程池顺序
         */
        protected static final AtomicInteger POOL_SEQ = new AtomicInteger(1);
        /**
         * 线程数
         */
        protected final AtomicInteger THREAD_NUM = new AtomicInteger(1);
        /**
         * 线程名称前缀
         */
        protected final String THREAD_NAME_PREFIX;
        /**
         * 是否开启守护线程
         */
        protected final boolean ENABLE_DAEMON;
        /**
         * 线程组
         */
        protected final ThreadGroup THREAD_GROUP;

        public NamedThreadFactory() {
            this("pool-" + POOL_SEQ.getAndIncrement(), false);
        }

        public NamedThreadFactory(String prefix) {
            this(prefix, false);
        }

        public NamedThreadFactory(String prefix, boolean daemon) {
            THREAD_NAME_PREFIX = prefix + "-thread-";
            ENABLE_DAEMON = daemon;
            THREAD_GROUP = Thread.currentThread().getThreadGroup();
        }

        @Override
        public Thread newThread(@NotNull Runnable runnable) {
            String name = THREAD_NAME_PREFIX + THREAD_NUM.getAndIncrement();
            Thread ret = new Thread(THREAD_GROUP, runnable, name, 0);
            ret.setDaemon(ENABLE_DAEMON);
            return ret;
        }

        public ThreadGroup getThreadGroup() {
            return THREAD_GROUP;
        }
    }
}
