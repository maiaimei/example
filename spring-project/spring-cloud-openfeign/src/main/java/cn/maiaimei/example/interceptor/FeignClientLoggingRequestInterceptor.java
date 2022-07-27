package cn.maiaimei.example.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class FeignClientLoggingRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String bodyAsString = requestTemplate.body() != null ? new String(requestTemplate.body(), StandardCharsets.UTF_8) : "";
        log.info("url: {}, method: {}, headers: {}, queries: {}, body: {}",
                requestTemplate.url(), requestTemplate.method(), requestTemplate.headers(), requestTemplate.queries(), bodyAsString);
    }
}
