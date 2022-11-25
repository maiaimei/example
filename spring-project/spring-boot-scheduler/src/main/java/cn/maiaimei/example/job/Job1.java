package cn.maiaimei.example.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnExpression("false")
public class Job1 {
    @Scheduled(cron = "0/10 * * * * ?", zone = "Asia/Shanghai")
    public void task01() {
        log.info("每 10 秒执行一次");
    }

    @Scheduled(cron = "0 0/10 * * * ?", zone = "Asia/Shanghai")
    public void task02() {
        log.info("每 10 分钟执行一次");
    }

    @Scheduled(cron = "0 5/10 * * * ?", zone = "Asia/Shanghai")
    public void task03() {
        log.info("从 5分钟 开始每 10 分钟执行一次");
    }
}
