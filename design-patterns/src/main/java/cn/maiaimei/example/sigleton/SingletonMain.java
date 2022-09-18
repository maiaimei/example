package cn.maiaimei.example.sigleton;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SingletonMain {
    @SneakyThrows
    public static void main(String[] args) {
        int cnt = 10;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(cnt);
        log.info("begin");
        for (int i = 0; i < cnt; i++) {
            executor.submit(() -> {
                // SingletonA instance = SingletonA.getInstance();
                // SingletonB instance = SingletonB.getInstance();
                SingletonC instance = SingletonC.getInstance();
                log.info("{}", instance);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executor.shutdown();
        log.info("end");
    }
}
