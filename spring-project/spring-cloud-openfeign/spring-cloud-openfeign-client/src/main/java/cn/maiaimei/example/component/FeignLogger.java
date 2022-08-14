package cn.maiaimei.example.component;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static feign.Util.UTF_8;
import static feign.Util.decodeOrDefault;

@Slf4j
public class FeignLogger extends feign.Logger {

    private ObjectMapper objectMapper = new ObjectMapper();

    public FeignLogger() {
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
    }

    /**
     * 自定义请求日志
     */
    @SneakyThrows
    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        Request.HttpMethod httpMethod = request.httpMethod();
        String url = request.url();
        String payload = StringUtils.EMPTY;
        if (request.body() != null) {
            payload = new String(request.body(), UTF_8);
        }
        Map<String, Collection<String>> headers = request.headers();
        String headerAsString = objectMapper.writeValueAsString(headers);
        log.info("{} ------> {} url:{}, headers:{}, payload:{}", configKey, httpMethod, url, headerAsString, payload);
    }

    /**
     * 自定义响应日志
     */
    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        String responseAsString = StringUtils.EMPTY;
        int status = response.status();
        try {
            if (response.body() != null) {
                byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                responseAsString = decodeOrDefault(bodyData, UTF_8, "Binary data");
                return response.toBuilder().body(bodyData).build();
            }
            return response;
        } finally {
            if (response.status() == 200) {
                log.info("{} <------ status:{}, response:{}, elapsedTime:[{}ms]", configKey, status, responseAsString, elapsedTime);
            } else {
                log.error("{} <------ status:{}, response:{}, elapsedTime:[{}ms]", configKey, status, responseAsString, elapsedTime);
            }
        }
    }
}