package com.sopp.all.config.thread;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName: StartThreadListener
 * @Description: 开启线程监听服务
 * @Author: Sopp
 * @Date: 2019/12/10 15:47
 **/
@Slf4j
@Component
public class ThreadPoolConfig {

    /**
     * 默认线程数
     */
    @Value("${thread.poolsize}")
    private int poolSize;

    /**
     * 最大线程数
     */
    @Value("${thread.maxsize}")
    private int maxSize;

    /**
     * 允许空闲时间,默认（秒）
     */
    @Value("${thread.keep-alive-time}")
    private int keepAliveTime;

    /**
     * 缓冲队列大小
     */
    @Value("${thread.capacity}")
    private int capacity;

    /**
     * 线程前缀名
     */
    @Value("${thread.name-prefix}")
    private String threadNamePrefix;

    /**
     * Description: 创建线程池的bean
     *
     * @param
     * @return org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
     * @throws
     * @auther Sopp
     * @date: 2019/12/10 16:18
     */
    @Bean("threadExec")
    private ThreadPoolTaskExecutor createThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(maxSize);
        executor.setQueueCapacity(capacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);

        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }
}