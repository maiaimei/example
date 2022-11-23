package cn.maiaimei.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncTaskConfig implements AsyncConfigurer {
    private static final Logger log = LoggerFactory.getLogger(AsyncTaskConfig.class);

    @Autowired
    ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor;

    @Override
    public Executor getAsyncExecutor() {
        //return AsyncConfigurer.super.getAsyncExecutor();
        return asyncThreadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        //return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
        return (throwable, method, objects) -> {
            log.error("异步任务执行出现异常, message: {}, method: {}, params: {}", throwable, method, objects);
        };
    }
}
