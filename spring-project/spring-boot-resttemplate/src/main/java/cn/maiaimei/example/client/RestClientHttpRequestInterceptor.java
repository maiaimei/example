package cn.maiaimei.example.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RestClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // 添加请求头
        HttpHeaders headers = request.getHeaders();
        headers.add("Cookie", "SESSIONID=b8dd5bd9-9fb7-48cb-a86b-e079cb554fb8");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ClientHttpResponse response = execution.execute(request, body);
        stopWatch.stop();

        // request body
        byte[] reqBody;
        if (request.getHeaders().getContentType() != null && request.getHeaders().getContentType().includes(MediaType.MULTIPART_FORM_DATA)) {
            reqBody = new byte[]{};
        } else {
            reqBody = body;
        }

        // response body
        StringBuilder resBody = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                resBody.append(line);
                line = bufferedReader.readLine();
            }
        }

        // 记录请求响应日志
        List<String> list = new ArrayList<>();
        list.add(formatLog("RequestUri", request.getURI().toString()));
        list.add(formatLog("RequestMethod", request.getMethodValue()));
        list.add(formatLog("RequestHeaders", request.getHeaders()));
        list.add(formatLog("RequestBody", new String(reqBody, StandardCharsets.UTF_8)));
        list.add(formatLog("ResponseStatus", response.getRawStatusCode()));
        list.add(formatLog("ResponseHeaders", response.getHeaders()));
        list.add(formatLog("ResponseBody", resBody.toString()));
        log.info("\n{}\ncompleted request in {} ms", String.join("\n", list), stopWatch.getTotalTimeMillis());

        return response;
    }

    private String formatLog(String key, Object value) {
        return String.format("%-15s: %s", key, value);
    }
}
