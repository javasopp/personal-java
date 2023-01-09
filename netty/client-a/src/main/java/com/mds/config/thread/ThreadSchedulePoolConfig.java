package com.mds.config.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/11/22 14:53
 */
@Slf4j
@Component
public class ThreadSchedulePoolConfig {

    @Bean
    public ThreadPoolTaskScheduler create() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        /**需要实例化线程*/
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }
}
