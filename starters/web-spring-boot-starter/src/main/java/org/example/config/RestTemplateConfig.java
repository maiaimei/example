package org.example.config;

import java.io.IOException;
import java.util.Collections;
import org.example.constant.GlobalConstants;
import org.example.utils.TraceIdUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setInterceptors(
        Collections.singletonList(new TraceIdClientHttpRequestInterceptor()));
    return restTemplate;
  }

  public static class TraceIdClientHttpRequestInterceptor
      implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
        ClientHttpRequestExecution execution) throws IOException {

      // 添加traceId到请求头
      request.getHeaders().add(
          GlobalConstants.REQUEST_HEADER_TRACE_ID,
          TraceIdUtils.getTraceId());

      return execution.execute(request, body);
    }
  }
}
