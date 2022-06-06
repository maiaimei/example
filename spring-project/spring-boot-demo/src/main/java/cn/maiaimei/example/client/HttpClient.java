package cn.maiaimei.example.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class HttpClient {
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T get(String url, Class<T> responseType, Object... uriVariables) {
        return this.exchange(url, HttpMethod.GET, responseType, uriVariables);
    }

    public <T> T get(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return this.exchange(url, HttpMethod.GET, responseType, uriVariables);
    }

    public <T> T post(String url, Object request, Class<T> responseType, Object... uriVariables) {
        return this.exchange(url, HttpMethod.POST, request, responseType, uriVariables);
    }

    public <T> T post(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) {
        return this.exchange(url, HttpMethod.POST, request, responseType, uriVariables);
    }

    public <T> T exchange(String url, HttpMethod method, Class<T> responseType, Object... uriVariables) {
        HttpEntity<?> requestEntity = buildHttpEntity(null, addHeaders());
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T> T exchange(String url, HttpMethod method, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpEntity<?> requestEntity = buildHttpEntity(requestBody, addHeaders());
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T> T exchange(String url, HttpMethod method, Object requestBody, Map<String, String> requestHeaders, Class<T> responseType, Object... uriVariables) {
        Map<String, String> headers = getAllHeaders(requestHeaders);
        HttpEntity<?> requestEntity = buildHttpEntity(requestBody, headers);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T> T exchange(String url, HttpMethod method, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<?> requestEntity = buildHttpEntity(null, addHeaders());
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T> T exchange(String url, HttpMethod method, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<?> requestEntity = buildHttpEntity(requestBody, addHeaders());
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T> T exchange(String url, HttpMethod method, Object requestBody, Map<String, String> requestHeaders, Class<T> responseType, Map<String, ?> uriVariables) {
        Map<String, String> headers = getAllHeaders(requestHeaders);
        HttpEntity<?> requestEntity = buildHttpEntity(requestBody, headers);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, Object requestBody,
                                          ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        // TODO
        return null;
    }

    public Map<String, String> addHeaders() {
        return null;
    }

    private Map<String, String> getAllHeaders(Map<String, String> requestHeaders) {
        Map<String, String> headers = addHeaders();
        if (headers == null || headers.isEmpty()) {
            return requestHeaders;
        }
        Map<String, String> allHeaders = new HashMap<>();
        allHeaders.putAll(headers);
        allHeaders.putAll(requestHeaders);
        return allHeaders;
    }

    private HttpHeaders buildHttpHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpHeaders.add(entry.getKey(), entry.getValue());
        }
        return httpHeaders;
    }

    private HttpEntity<?> buildHttpEntity(Object body, Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return new HttpEntity<>(body);
        }
        return new HttpEntity<>(body, buildHttpHeaders(headers));
    }
}
