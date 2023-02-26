package cn.maiaimei.example.sigleton;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SingletonApplication {
    @SneakyThrows
    public static void main(String[] args) {
        int cnt = 10;
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(cnt);
        for (int i = 0; i < cnt; i++) {
            executor.submit(() -> {
                //Singleton1 instance = Singleton1.getInstance();
                //Singleton2 instance = Singleton2.getInstance();
                //Singleton3 instance = Singleton3.getInstance();
                //Singleton4 instance = Singleton4.getInstance();
                Singleton5 instance = Singleton5.getInstance();
                log.info("{}", instance);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executor.shutdown();
    }
}
