package cn.maiaimei.example.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {
    /**
     * 用来记录接口执行时间的最小接收值
     */
    private long timeoutMillis = -1;

    public LoggingRequestInterceptor() {

    }

    public LoggingRequestInterceptor(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            ClientHttpResponse response = execution.execute(request, body);
            stopWatch.stop();
            long totalTimeMillis = stopWatch.getTotalTimeMillis();
            if (timeoutMillis != -1 && totalTimeMillis > timeoutMillis) {
                log.warn("Request uri: [{}]{}, Cost time: {}ms", request.getMethod(), request.getURI(), totalTimeMillis);
            }
            String responseStr = response != null ? IOUtils.toString(response.getBody(), StandardCharsets.UTF_8) : StringUtils.EMPTY;
            log.info("Request uri: [{}]{}, Headers: {}, Param: {}, Status: {}, Response: {}",
                    request.getMethod(), request.getURI(), request.getHeaders(), new String(body, StandardCharsets.UTF_8), response.getStatusCode(), responseStr);
            return response;
        } catch (Exception e) {
            log.error("Request uri: [{}]{}, Headers: {}, Param: {}",
                    request.getMethod(), request.getURI(), request.getHeaders(), new String(body, StandardCharsets.UTF_8), e);
            throw e;
        }
    }
}