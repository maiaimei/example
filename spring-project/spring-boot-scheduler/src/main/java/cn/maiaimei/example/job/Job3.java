package cn.maiaimei.example.job;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;

@Slf4j
@Component
public class Job3 {
    @PostConstruct
    public void init() {
        Calendar c = Calendar.getInstance();
        int minute = c.get(Calendar.MINUTE);
        boolean isBetween0And25Minute = 0 < minute && minute < 25;
        boolean isBetween30And55Minute = 30 < minute && minute < 55;
        if (isBetween0And25Minute || isBetween30And55Minute) {
            log.info("项目启动时执行一次，拉取并存储文档");
        }
    }

    @SchedulerLock(name = "Job3_task01", lockAtMostFor = 30 * 60 * 1000, lockAtLeastFor = 30 * 60 * 1000)
    @Scheduled(cron = "0 0/30 * * * ?", zone = "Asia/Shanghai")
    public void task01() {
        log.info("每 30 分钟执行一次，拉取并存储文档");
    }

    /**
     * 00 10 20 30 40 50
     */
    @SchedulerLock(name = "Job3_task02", lockAtMostFor = 10 * 60 * 1000, lockAtLeastFor = 10 * 60 * 1000)
    @Scheduled(cron = "0 0/10 * * * ?", zone = "Asia/Shanghai")
    public void task02() {
        log.info("每 10 分钟执行一次，下载并上传文件");
    }

    /**
     * 00 05 10 15 20 25 30 35 40 45 50 55
     */
    @SchedulerLock(name = "Job3_task03", lockAtMostFor = 5 * 60 * 1000, lockAtLeastFor = 5 * 60 * 1000)
    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void task03() {
        log.info("每 5 分钟执行一次，更新文档状态");
    }

    /**
     * 00 05 10 15 20 25 30 35 40 45 50 55
     */
    @SchedulerLock(name = "Job3_task04", lockAtMostFor = 5 * 60 * 1000, lockAtLeastFor = 5 * 60 * 1000)
    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void task04() {
        log.info("每 5 分钟执行一次，创建工作项");
    }

    /**
     * 00 05 10 15 20 25 30 35 40 45 50 55
     */
    @SchedulerLock(name = "Job3_task05", lockAtMostFor = 5 * 60 * 1000, lockAtLeastFor = 5 * 60 * 1000)
    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void task05() {
        log.info("每 5 分钟执行一次，发送邮件");
    }
}
