package cn.maiaimei.example.job;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.Calendar;

@Slf4j
//@Component
public class Job1 {
    //@Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void task01() {
        log.info("每 2 秒执行一次");
    }

    //@Scheduled(cron = "0 0/2 * * * ?", zone = "Asia/Shanghai")
    public void task02() {
        log.info("每 2 分钟执行一次");
    }

    //@Scheduled(cron = "0 5/10 * * * ?", zone = "Asia/Shanghai")
    public void task03() {
        log.info("从 5分钟 开始每 10 分钟执行任务");
    }

    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void task04() {
        log.info("每 5 分钟执行一次");
    }

    @Scheduled(cron = "0 0/10 * * * ?", zone = "Asia/Shanghai")
    public void task06() {
        log.info("每 10 分钟执行一次");
    }

    @PostConstruct
    @SchedulerLock(name = "Task05_Job", lockAtMostFor = 30 * 60 * 1000, lockAtLeastFor = 30 * 60 * 1000)
    @Scheduled(cron = "0 0/30 * * * ?", zone = "Asia/Shanghai")
    public void task05() {
        log.info("项目启动时执行一次，之后每 30 分钟执行一次");
    }


    @PostConstruct
    public void task09_01() {
        Calendar c = Calendar.getInstance();
        int minute = c.get(Calendar.MINUTE);
        boolean isBetween0And25Minute = 0 < minute && minute < 25;
        boolean isBetween30And55Minute = 30 < minute && minute < 55;
        if (isBetween0And25Minute || isBetween30And55Minute) {
            log.info("项目启动时执行一次");
        }
    }

    @SchedulerLock(name = "Task09_Job", lockAtMostFor = 30 * 60 * 1000, lockAtLeastFor = 30 * 60 * 1000)
    @Scheduled(cron = "0 0/30 * * * ?", zone = "Asia/Shanghai")
    public void task09_02() {
        log.info("每 30 分钟执行一次");
    }


    /**
     * @SchedulerLock(name, lockAtMostFor, lockAtLeastFor)
     * name = 任务名称必须唯一
     * lockAtMostFor = 最长锁表时间（防止节点奔溃，不释放锁）
     * lockAtLeastFor = 最短锁表时间（防止任务重复跑）
     */
    //@SchedulerLock(name = "Task07_Job", lockAtMostFor = 2 * 1000, lockAtLeastFor = 2 * 1000)
    //@Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void task07() {
        log.info("每 2 秒执行一次");
    }

    //@SchedulerLock(name = "Task08_Job", lockAtMostFor = 5 * 1000, lockAtLeastFor = 5 * 1000)
    //@Scheduled(cron = "0/5 * * * * ?", zone = "Asia/Shanghai")
    public void task08() {
        log.info("每 5 秒执行一次");
    }
}
