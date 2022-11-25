package cn.maiaimei.example.controller;

import cn.maiaimei.example.component.AsyncHelper;
import cn.maiaimei.example.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/async")
public class AsyncController {
    private static final Logger log = LoggerFactory.getLogger(AsyncController.class);

    private final AsyncHelper asyncHelper;
    private final AsyncService asyncService;

    public AsyncController(AsyncHelper asyncHelper,
                           AsyncService asyncService) {
        this.asyncHelper = asyncHelper;
        this.asyncService = asyncService;
    }

    @GetMapping("/test1")
    public String test1() {
        log.info("===> exec async method {}", System.currentTimeMillis());
        asyncService.test1();
        log.info("===> exec async method {}", System.currentTimeMillis());
        return "ok";
    }

    @GetMapping("/test2")
    public String test2(@RequestParam(required = false) String n) {
        log.info("===> exec async method {}", System.currentTimeMillis());
        asyncService.test2(n);
        log.info("===> exec async method {}", System.currentTimeMillis());
        return "ok";
    }

    @GetMapping("/test3")
    public String test3(@RequestParam(required = false) String n) {
        log.info("===> exec async method {}", System.currentTimeMillis());
        CompletableFuture<Void> future3 = asyncHelper.call(asyncService::test3);
        CompletableFuture<Void> future4 = asyncHelper.call(() -> asyncService.test4(n));
        asyncHelper.await(future3, future4);
        log.info("===> exec async method {}", System.currentTimeMillis());
        return "ok";
    }
}
