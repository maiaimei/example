package cn.maiaimei.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class HttpClient {
    private RestTemplate restTemplate;

    @Autowired
    public HttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T send(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForObject(getUrl(url), responseType, uriVariables);
    }

    public <T> T send(String url, Object requestBody, Class<T> responseType, Object... uriVariables) throws RestClientException {
        return restTemplate.postForObject(getUrl(url), requestBody, responseType, uriVariables);
    }

    public <T> T send(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.getForObject(getUrl(url, uriVariables), responseType);
    }

    public <T> T send(String url, Map<String, String> headers, Class<T> responseType, Map<String, ?> uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url, uriVariables), HttpMethod.GET, getHttpEntity(null, headers), responseType);
        return responseEntity.getBody();
    }

    public <T> T send(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        return restTemplate.postForObject(getUrl(url, uriVariables), requestBody, responseType);
    }

    public <T> T send(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url, uriVariables), HttpMethod.POST, getHttpEntity(requestBody, headers), responseType);
        return responseEntity.getBody();
    }

    public <T, U> T send(String url, HttpMethod method, Map<String, String> headers, U requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url, uriVariables), method, getHttpEntity(requestBody, headers), responseType);
        return responseEntity.getBody();
    }

    public <T> T send(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url), HttpMethod.GET, null, responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T> T send(String url, Object requestBody, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url), HttpMethod.POST, getHttpEntity(requestBody, null), responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T> T send(String url, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url, uriVariables), HttpMethod.GET, null, responseType);
        return responseEntity.getBody();
    }

    public <T> T send(String url, Map<String, String> headers, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url, uriVariables), HttpMethod.GET, getHttpEntity(null, headers), responseType);
        return responseEntity.getBody();
    }

    public <T> T get(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.getForObject(getUrl(url), responseType, uriVariables);
    }

    public <T> T get(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForObject(getUrl(url), responseType, uriVariables);
    }

    public <T> T get(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url), HttpMethod.GET, null, responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T> T post(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        return restTemplate.postForObject(getUrl(url), requestBody, responseType, uriVariables);
    }

    public <T> T post(String url, Object requestBody, Class<T> responseType, Object... uriVariables) {
        return restTemplate.postForObject(getUrl(url), requestBody, responseType, uriVariables);
    }

    public <T> T post(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url), HttpMethod.POST, null, responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T, U> T exchange(String url, HttpMethod method, Class<T> responseType, Object... uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url), method, null, responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T, U> T exchange(String url, HttpMethod method, U requestBody, Class<T> responseType, Object... uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url), method, getHttpEntity(requestBody, null), responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T, U> T exchange(String url, HttpMethod method, U requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url), method, getHttpEntity(requestBody, null), responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T, U> T exchange(String url, HttpMethod method, Map<String, String> headers, U requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url), method, getHttpEntity(requestBody, headers), responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T, U> T exchange(String url, HttpMethod method, Map<String, String> headers, U requestBody, Class<T> responseType, Object... uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url), method, getHttpEntity(requestBody, headers), responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T, U> T exchange(String url, HttpMethod method, U requestBody, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url), method, getHttpEntity(requestBody, null), responseType, uriVariables);
        return responseEntity.getBody();
    }

    public <T, U> T exchange(String url, HttpMethod method, Map<String, String> headers, U requestBody, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(getUrl(url), method, getHttpEntity(requestBody, headers), responseType, uriVariables);
        return responseEntity.getBody();
    }

    protected String getBaseUrl() {
        return null;
    }

    protected Map<String, String> getHeaders() {
        return null;
    }

    private String getUrl(String url) {
        String baseUrl = getBaseUrl();
        if (StringUtils.hasText(baseUrl)) {
            return baseUrl + url;
        }
        return url;
    }

    private String getUrl(String url, Map<String, ?> uriVariables) {
        String baseUrl = getBaseUrl();
        if (StringUtils.hasText(baseUrl)) {
            url = baseUrl + url;
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        if (uriVariables != null && uriVariables.size() > 0) {
            for (Map.Entry<String, ?> entry : uriVariables.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
        }
        return builder.toUriString();
    }

    private <T> HttpEntity<T> getHttpEntity(T body, Map<String, String> headers) {
        Map<String, String> allHeaders = new HashMap<>();
        if (headers != null) {
            allHeaders.putAll(headers);
        }
        if (getHeaders() != null) {
            allHeaders.putAll(getHeaders());
        }
        if (allHeaders.size() > 0) {
            MultiValueMap<String, String> map = new HttpHeaders();
            for (Map.Entry<String, String> entry : allHeaders.entrySet()) {
                map.put(entry.getKey(), Collections.singletonList(entry.getValue()));
            }
            return new HttpEntity<>(body, map);
        }
        return new HttpEntity<>(body, null);
    }
}
