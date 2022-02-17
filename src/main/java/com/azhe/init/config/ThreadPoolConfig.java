package com.azhe.init.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2022/1/10 12:01 下午
 * @since V2.0.0
 */
@Configuration
public class ThreadPoolConfig {

    // 线程数
    private final int threadCount = Runtime.getRuntime().availableProcessors() * 2;

    @Bean("threadPool")
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                threadCount, threadCount, 20, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(25000),
                new ThreadFactory() {
                    int i = 0;
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "pc-menu-init-thread-pool-" + i++);
                    }
                });
    }

}
