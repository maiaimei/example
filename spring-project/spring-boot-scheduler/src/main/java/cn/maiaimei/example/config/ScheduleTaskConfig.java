package cn.maiaimei.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 多线程执行定时任务
 */
@Configuration
public class ScheduleTaskConfig implements SchedulingConfigurer {
    @Autowired
    SchedulingTaskExecutor schedulingTaskExecutor;

    //private static final int FIVE = 5;

    /**
     * 应用场景：
     * 一个定时器类中有n个定时任务，有每30秒执行一次的还有每1分钟执行一次的，出现问题的定时任务是0点整时执行的定时任务到了0点没有执行。
     * <p>
     * 原因分析：
     * spring定时器任务scheduled-tasks默认配置是单线程串行执行的，
     * 当某个定时任务出现阻塞，或者执行时间过长，则线程就会被占用，
     * 其他定时任务排队执行，导致后面的定时任务未能准时执行。
     * <p>
     * spring task的调度任务线程默认是单线程的，
     * 定时任务需要排队，需要一个任务完成之后才能再执行另一个定时任务，
     * 如果先开始的任务在另一个任务需要开始的时候，还没执行完，那么后边的这个任务是没有执行的，
     * 如果需要异步执行这两个定时任务，就需要自定义设置一下执行定时任务的线程池
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        // scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(FIVE));
        scheduledTaskRegistrar.setScheduler(schedulingTaskExecutor);
    }
}
