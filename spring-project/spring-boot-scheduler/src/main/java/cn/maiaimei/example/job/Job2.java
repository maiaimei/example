package cn.maiaimei.example.job;

import cn.maiaimei.example.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @SchedulerLock(name, lockAtMostFor, lockAtLeastFor)
 * name = 任务名称必须唯一
 * lockAtMostFor = 最长锁表时间（防止节点奔溃，不释放锁）
 * lockAtLeastFor = 最短锁表时间（防止任务重复跑）
 */
@Slf4j
@Component
@ConditionalOnExpression("true")
public class Job2 implements CommandLineRunner {
    private final AsyncService asyncService;

    public Job2(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @SchedulerLock(name = "Job2_task01", lockAtMostFor = "PT29M59S", lockAtLeastFor = "PT29M58S")
    @Scheduled(cron = "0 0/30 * * * ?", zone = "Asia/Shanghai")
    public void task01() {
        LockAssert.assertLocked();
        log.info("===> 每 30 分钟执行一次，拉取并存储文档");
        log.info("<=== 每 30 分钟执行一次，拉取并存储文档");
    }

    @SchedulerLock(name = "Job2_task02_2", lockAtMostFor = "PT29M59S", lockAtLeastFor = "PT29M58S")
    @Scheduled(cron = "0 0/30 * * * ?", zone = "Asia/Shanghai")
    public void task02_2() {
        LockAssert.assertLocked();
        log.info("===> 每 30 分钟执行一次，下载上传文件");
        log.info("<=== 每 30 分钟执行一次，下载上传文件");
    }

    @SchedulerLock(name = "Job2_task02", lockAtMostFor = "PT9M59S", lockAtLeastFor = "PT9M58S")
    @Scheduled(cron = "0 0/10 * * * ?", zone = "Asia/Shanghai")
    public void task02() {
        LockAssert.assertLocked();
        log.info("===> 每 10 分钟执行一次，下载上传文件");
        log.info("<=== 每 10 分钟执行一次，下载上传文件");
    }

    @SchedulerLock(name = "Job2_task03", lockAtMostFor = "PT4M59S", lockAtLeastFor = "PT4M58S")
    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void task03() {
        LockAssert.assertLocked();
        log.info("===> 每 5 分钟执行一次，更新文档状态");
        log.info("<=== 每 5 分钟执行一次，更新文档状态");
    }

    @SchedulerLock(name = "Job2_task04", lockAtMostFor = "PT4M59S", lockAtLeastFor = "PT4M58S")
    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void task04() {
        LockAssert.assertLocked();
        log.info("===> 每 5 分钟执行一次，创建工作项");
        log.info("<=== 每 5 分钟执行一次，创建工作项");
    }

    @SchedulerLock(name = "Job2_task05", lockAtMostFor = "PT4M59S", lockAtLeastFor = "PT4M58S")
    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void task05() {
        LockAssert.assertLocked();
        log.info("===> 每 5 分钟执行一次，发送邮件");
        log.info("<=== 每 5 分钟执行一次，发送邮件");
    }

    @SchedulerLock(name = "Job2_task06", lockAtMostFor = "PT4M59S", lockAtLeastFor = "PT4M58S")
    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void task06() {
        LockAssert.assertLocked();
        log.info("===> 每 5 分钟执行一次异步任务");
        asyncService.test2(null);
        log.info("<=== 每 5 分钟执行一次异步任务");
    }

    @Override
    public void run(String... args) throws Exception {
        // 分布式部署时，这里可能不能保证幂等性
//        Calendar c = Calendar.getInstance();
//        int minute = c.get(Calendar.MINUTE);
//        boolean isBetween0And25Minute = 0 < minute && minute < 25;
//        boolean isBetween30And55Minute = 30 < minute && minute < 55;
//        if (isBetween0And25Minute || isBetween30And55Minute) {
//            log.info("项目启动时执行一次，拉取并存储文档");
//        }
    }
}
