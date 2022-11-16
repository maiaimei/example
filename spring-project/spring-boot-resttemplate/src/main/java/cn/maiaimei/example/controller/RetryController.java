package cn.maiaimei.example.controller;

import cn.maiaimei.example.client.HttpClient;
import cn.maiaimei.example.service.DemoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

@Api
@RestController
@RequestMapping("/retry")
public class RetryController {
    @Autowired
    private HttpClient httpClient;

    @Autowired
    private DemoService demoService;

    @Retryable(value = {RestClientException.class}, maxAttempts = 3, backoff = @Backoff(value = 2000, multiplier = 2))
    @GetMapping("/http")
    public void httpRetry() {
        httpClient.get("http://localhost:8080/xxx", String.class);
    }

    @GetMapping("/generalMethod")
    public int generalMethodRetry() {
        return demoService.test();
    }
}
