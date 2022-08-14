package cn.maiaimei.example.component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    private static final String TRACE_ID = "traceId";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 全链路跟踪
        requestTemplate.header(TRACE_ID, MDC.get(TRACE_ID));
    }
}