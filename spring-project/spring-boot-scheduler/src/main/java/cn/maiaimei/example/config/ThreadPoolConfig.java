package cn.maiaimei.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 * <p>
 * springboot项目自定义设置执行定时任务与异步任务的线程池：https://blog.csdn.net/u011174699/article/details/123753911
 */
@Configuration
public class ThreadPoolConfig {
    /**
     * 异步任务线程池配置
     */
    private static final Integer ASYNC_CORE_POOL_SIZE = 5;
    private static final Integer ASYNC_MAX_POOL_SIZE = 5;
    private static final Integer ASYNC_QUEUE_CAPACITY = 2000;
    private static final Integer ASYNC_KEEP_ALIVE_SECONDS = 60;
    private static final String ASYNC_THREAD_NAME_PREFIX = "async-";

    /**
     * 定时任务线程池配置
     */
    private static final Integer SCHEDULER_POOL_SIZE = 5;
    private static final String SCHEDULER_THREAD_NAME_PREFIX = "scheduler-";


    /**
     * 创建执行spring task定时任务的线程池，调用@Scheduled注解的定时任务
     *
     * @return
     */
    @Bean
    public SchedulingTaskExecutor schedulingTaskExecutor() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(SCHEDULER_POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix(SCHEDULER_THREAD_NAME_PREFIX);
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        return threadPoolTaskScheduler;
    }


    /**
     * 创建执行异步任务的线程池，用于调用 @Async("asyncThreadPoolTaskExecutor")注解的方法
     *
     * @return
     */
    @Bean("asyncThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数量
        threadPoolTaskExecutor.setCorePoolSize(ASYNC_CORE_POOL_SIZE);
        // 最大线程数量
        threadPoolTaskExecutor.setMaxPoolSize(ASYNC_MAX_POOL_SIZE);
        // 队列中最大任务数
        threadPoolTaskExecutor.setQueueCapacity(ASYNC_QUEUE_CAPACITY);
        //  线程名称前缀
        threadPoolTaskExecutor.setThreadNamePrefix(ASYNC_THREAD_NAME_PREFIX);
        // 当达到最大线程数时如何处理新任务（拒绝策略）
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程空闲后最大存活时间
        threadPoolTaskExecutor.setKeepAliveSeconds(ASYNC_KEEP_ALIVE_SECONDS);
        // 初始化线程池
        threadPoolTaskExecutor.initialize();
        // 关闭线程池
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return threadPoolTaskExecutor;
    }
}
