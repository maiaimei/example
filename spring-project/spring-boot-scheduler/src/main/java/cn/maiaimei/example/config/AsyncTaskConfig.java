package cn.maiaimei.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncTaskConfig implements AsyncConfigurer {
    private static final Logger log = LoggerFactory.getLogger(AsyncTaskConfig.class);

    @Autowired
    ThreadPoolTaskExecutor asyncTaskExecutor;

    /**
     * 返回用于执行异步方法的线程池
     *
     * @return
     */
    @Override
    public Executor getAsyncExecutor() {
        return asyncTaskExecutor;
    }

    /**
     * 当线程池执行异步任务时会抛出AsyncUncaughtExceptionHandler异常，
     * 此方法会捕获该异常
     * 只能捕获 @Async("asyncTaskExecutor") 标记的异步方法抛出的异常
     * 无法捕获 AsyncHelper.call(...) 抛出的异常
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            log.error("异步任务异常, 方法: {}, 参数: {}", method, objects, throwable);
        };
    }
}
