package cn.maiaimei.example.service;

import cn.maiaimei.example.constant.GlobalConstant;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AsyncService {
    private static final Logger log = LoggerFactory.getLogger(AsyncService.class);

    @Async(GlobalConstant.ASYNC_TASK_EXECUTOR)
    public void test1() {
        testA();
    }

    @Async(GlobalConstant.ASYNC_TASK_EXECUTOR)
    public void test2(String n) {
        testB(n);
    }

    public void test3() {
        testA();
    }

    public void test4(String n) {
        testB(n);
    }

    @SneakyThrows
    private void testA() {
        log.info("<=== exec async method {}", System.currentTimeMillis());
        TimeUnit.MILLISECONDS.sleep(500L);
        log.info("<=== exec async method {}", System.currentTimeMillis());
    }

    @SneakyThrows
    private void testB(String n) {
        log.info("<=== exec async method {}", System.currentTimeMillis());
        TimeUnit.MILLISECONDS.sleep(500L);
        log.info("<=== exec async method {}", System.currentTimeMillis());

        // mock exception
        int i = StringUtils.hasText(n) ? Integer.parseInt(n) : new Random().nextInt(2);
        int r = 1 / i;
        log.info("r={}", r);
    }
}
