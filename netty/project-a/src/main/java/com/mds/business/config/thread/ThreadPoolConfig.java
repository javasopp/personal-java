package com.mds.business.config.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/5/31 09:06
 */
@Slf4j
@EnableAsync
@Configuration
public class ThreadPoolConfig {
    /**
     * Description: 创建线程池的bean
     *
     * @param
     * @return org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
     * @throws
     * @auther Sopp
     * @date: 2019/12/10 16:18
     */
    @Bean
    public ThreadPoolTaskExecutor createThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(10);
        executor.setThreadNamePrefix("thread-pool");

        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

}
