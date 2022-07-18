package cn.maiaimei.example.service;

import cn.maiaimei.example.util.Axios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class DemoService {
    @Autowired
    private Axios axios;

    @Autowired
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Autowired
    private OtherService otherService;

    public String methodA() {
        // 异步操作
        CompletableFuture<String> methodAResultFuture = CompletableFuture.supplyAsync(() -> otherService.methodA(), asyncTaskExecutor);
        CompletableFuture<String> methodBResultFuture = CompletableFuture.supplyAsync(() -> otherService.methodB(), asyncTaskExecutor);
        // 等待执行完毕
        CompletableFuture.allOf(methodAResultFuture, methodBResultFuture).join();
        return methodAResultFuture.join() + ", " + methodBResultFuture.join();
    }

    public String methodB() {
        // 异步操作
        CompletableFuture<String> methodAResultFuture = axios.call(() -> otherService.methodA());
        CompletableFuture<String> methodBResultFuture = axios.call(() -> otherService.methodB());
        // 等待执行完毕
        axios.await(methodAResultFuture, methodBResultFuture);
        return methodAResultFuture.join() + ", " + methodBResultFuture.join();
    }
}