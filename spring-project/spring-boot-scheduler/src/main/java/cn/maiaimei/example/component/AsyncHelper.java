package cn.maiaimei.example.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Component
public class AsyncHelper {
    @Autowired
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    public CompletableFuture<Void> call(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, asyncTaskExecutor);
    }

    public <U> CompletableFuture<U> call(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier, asyncTaskExecutor);
    }

    public void await(CompletableFuture<?>... cfs) {
        CompletableFuture.allOf(cfs).join();
    }
}
