package cn.maiaimei.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class HttpClient {
    private RestTemplate restTemplate;

    @Autowired
    public HttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T get(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForObject(url, responseType, uriVariables);
    }

    public <T> T get(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.getForObject(url, responseType, uriVariables);
    }

    public <T> T post(String url, @Nullable Object requestBody, Class<T> responseType, Object... uriVariables) {
        return restTemplate.postForObject(url, requestBody, responseType, uriVariables);
    }

    public <T> T post(String url, @Nullable Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        return restTemplate.postForObject(url, requestBody, responseType, uriVariables);
    }

    protected String getBaseUrl() {
        return "";
    }
}
