package cn.maiaimei.example.service;

import cn.maiaimei.example.util.Axios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {
    @Autowired
    private Axios axios;

    @Autowired
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Autowired
    private XxxService xxxService;

    public String asyncMethod1() {
        // 异步操作
        CompletableFuture<String> methodAResultFuture = CompletableFuture.supplyAsync(() -> xxxService.hello(), asyncTaskExecutor);
        CompletableFuture<String> methodBResultFuture = CompletableFuture.supplyAsync(() -> xxxService.world(), asyncTaskExecutor);
        // 等待执行完毕
        CompletableFuture.allOf(methodAResultFuture, methodBResultFuture).join();
        return methodAResultFuture.join() + ", " + methodBResultFuture.join();
    }

    public String asyncMethod2() {
        // 异步操作
        CompletableFuture<String> methodAResultFuture = axios.call(() -> xxxService.hello());
        CompletableFuture<String> methodBResultFuture = axios.call(() -> xxxService.world());
        // 等待执行完毕
        axios.await(methodAResultFuture, methodBResultFuture);
        return methodAResultFuture.join() + ", " + methodBResultFuture.join();
    }
}
