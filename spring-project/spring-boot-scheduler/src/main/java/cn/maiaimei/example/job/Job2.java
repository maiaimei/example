package cn.maiaimei.example.job;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

@Slf4j
//@Component
public class Job2 {
    @PostConstruct
    public void init() {
        log.info("项目启动时执行一次，拉取并存储文档");
    }

    @SchedulerLock(name = "Job2_task01", lockAtMostFor = 30 * 60 * 1000, lockAtLeastFor = 30 * 60 * 1000)
    @Scheduled(cron = "0 0/30 * * * ?", zone = "Asia/Shanghai")
    public void task01() {
        log.info("每 30 分钟执行一次，拉取并存储文档");
    }

    @SchedulerLock(name = "Job2_task02", lockAtMostFor = 10 * 60 * 1000, lockAtLeastFor = 10 * 60 * 1000)
    @Scheduled(cron = "0 0/10 * * * ?", zone = "Asia/Shanghai")
    public void task02() {
        log.info("每 10 分钟执行一次，下载并上传文件");
    }

    @SchedulerLock(name = "Job2_task03", lockAtMostFor = 5 * 60 * 1000, lockAtLeastFor = 5 * 60 * 1000)
    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void task03() {
        log.info("每 5 分钟执行一次，更新文档状态");
    }

    @SchedulerLock(name = "Job2_task04", lockAtMostFor = 5 * 60 * 1000, lockAtLeastFor = 5 * 60 * 1000)
    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void task04() {
        log.info("每 5 分钟执行一次，创建工作项");
    }

    @SchedulerLock(name = "Job2_task05", lockAtMostFor = 5 * 60 * 1000, lockAtLeastFor = 5 * 60 * 1000)
    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void task05() {
        log.info("每 5 分钟执行一次，发送邮件");
    }
}
