package cn.maiaimei.example.job;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Job {
    //@Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void task01() {
        log.info("0/2 * * * * ? 表示每2秒执行任务");
    }

    //@Scheduled(cron = "0 0/2 * * * ?", zone = "Asia/Shanghai")
    public void task02() {
        log.info("0 0/2 * * * ? 表示每2分钟执行任务");
    }

    /**
     * @SchedulerLock(name = 任务名称必须唯一, lockAtMostFor = 最长锁表时间（防止节点奔溃，不释放锁）, lockAtLeastFor = 最短锁表时间（防止任务重复跑）)
     */
    @SchedulerLock(name = "Task03_Job", lockAtMostFor = 5 * 1000, lockAtLeastFor = 5 * 1000)
    @Scheduled(cron = "0/5 * * * * ?", zone = "Asia/Shanghai")
    public void task03() {
        log.info("0/5 * * * * ? 表示每5秒执行任务" + System.currentTimeMillis());
    }

    @SchedulerLock(name = "Task04_Job", lockAtMostFor = 2 * 1000, lockAtLeastFor = 2 * 1000)
    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void task04() {
        log.info("0/2 * * * * ? 表示每2秒执行任务" + System.currentTimeMillis());
    }
}
