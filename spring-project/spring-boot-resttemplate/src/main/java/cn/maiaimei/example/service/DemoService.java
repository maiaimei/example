package cn.maiaimei.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class DemoService {

    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(value = 2000, multiplier = 2))
    public int test() {
        Random random = new Random();
        int i = random.nextInt(2);
        log.info("被除数是：{}, 当前时间：{}", i, System.currentTimeMillis());
        return 1 / i;
    }

}
