package cn.maiaimei.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Component
public class Axios {
    @Autowired
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    public <U> CompletableFuture<U> call(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(() -> supplier.get(), asyncTaskExecutor);
    }

    public void await(CompletableFuture<?>... cfs) {
        CompletableFuture.allOf(cfs).join();
    }
}