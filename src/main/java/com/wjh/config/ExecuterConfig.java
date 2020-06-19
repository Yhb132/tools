package com.wjh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Classname Executer
 * @Description
 * @Date 2019-12-24 13:50
 * @Created by wjh
 */
@Configuration
public class ExecuterConfig {

    /**
     * 使用springboot注解异步，
     * 1.指定线程池
     * 2.不指定：只配置了一个线程池使用该线程池，多个使用默认的Sim**线程池
     *
     * @return
     */
    @Bean(name = "newUserExecutorHHH")
    public static ThreadPoolTaskExecutor newUserActivityExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.initialize();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setMaxPoolSize(7);
        //阻塞队列数
        taskExecutor.setQueueCapacity(4);
        return taskExecutor;
    }

    @Bean(name = "oldUserExecutorHHH")
    public static ThreadPoolTaskExecutor oldUserActivityExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.initialize();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setMaxPoolSize(7);
        //阻塞队列数
        taskExecutor.setQueueCapacity(4);
        return taskExecutor;
    }

}
