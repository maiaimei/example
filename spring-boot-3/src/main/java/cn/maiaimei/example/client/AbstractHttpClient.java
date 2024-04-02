package cn.maiaimei.example.client;

import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractHttpClient {

  protected final Logger log;

  private final RestTemplate restTemplate;

  public AbstractHttpClient(RestTemplate restTemplate) {
    this.log = LoggerFactory.getLogger(getClass().getName());
    this.restTemplate = restTemplate;
  }

  public <T> T delete(String url, Class<T> responseType, Object... uriVariables) {
    return exchange(url, HttpMethod.DELETE, null, responseType, uriVariables);
  }

  public <T> T delete(String url, Class<T> responseType, Map<String, ?> uriVariables) {
    return exchange(url, HttpMethod.DELETE, null, responseType, uriVariables);
  }

  public <T> T delete(String url, Class<T> responseType) {
    return exchange(url, HttpMethod.DELETE, null, responseType);
  }

  public <T> T get(String url, Class<T> responseType, Object... uriVariables) {
    return exchange(url, HttpMethod.GET, null, responseType, uriVariables);
  }

  public <T> T get(String url, Class<T> responseType, Map<String, ?> uriVariables) {
    return exchange(url, HttpMethod.GET, null, responseType, uriVariables);
  }

  public <T> T get(String url, Class<T> responseType) {
    return exchange(url, HttpMethod.GET, null, responseType);
  }

  public <T> T patch(String url, Object request, Class<T> responseType, Object... uriVariables) {
    return exchange(url, HttpMethod.PATCH, request, responseType, uriVariables);
  }

  public <T> T patch(String url, Object request, Class<T> responseType,
      Map<String, ?> uriVariables) {
    return exchange(url, HttpMethod.PATCH, request, responseType, uriVariables);
  }

  public <T> T patch(String url, Object request, Class<T> responseType) {
    return exchange(url, HttpMethod.PATCH, request, responseType);
  }

  public <T> T post(String url, Object request, Class<T> responseType, Object... uriVariables) {
    return exchange(url, HttpMethod.POST, request, responseType, uriVariables);
  }

  public <T> T post(String url, Object request, Class<T> responseType,
      Map<String, ?> uriVariables) {
    return exchange(url, HttpMethod.POST, request, responseType, uriVariables);
  }

  public <T> T post(String url, Object request, Class<T> responseType) {
    return exchange(url, HttpMethod.POST, request, responseType);
  }

  public <T> T put(String url, Object request, Class<T> responseType, Object... uriVariables) {
    return exchange(url, HttpMethod.PUT, request, responseType, uriVariables);
  }

  public <T> T put(String url, Object request, Class<T> responseType,
      Map<String, ?> uriVariables) {
    return exchange(url, HttpMethod.PUT, request, responseType, uriVariables);
  }

  public <T> T put(String url, Object request, Class<T> responseType) {
    return exchange(url, HttpMethod.PUT, request, responseType);
  }

  public <T> T exchange(String url, HttpMethod method, Object request,
      Class<T> responseType, Object... uriVariables) {
    return exchange(url, method, request, requestEntity -> this.restTemplate.exchange(
        url, method, requestEntity, responseType, uriVariables
    ));
  }

  public <T> T exchange(String url, HttpMethod method, Object request,
      Class<T> responseType, Map<String, ?> uriVariables) {
    return exchange(url, method, request, requestEntity -> this.restTemplate.exchange(
        url, method, requestEntity, responseType, uriVariables
    ));
  }

  private <T> T exchange(String url, HttpMethod method, Object request,
      Function<HttpEntity<?>, ResponseEntity<T>> function) {
    HttpEntity<?> requestEntity = new HttpEntity<>(request, getHeaders());
    logRequest(url, method, requestEntity);
    final ResponseEntity<T> responseEntity = function.apply(requestEntity);
    logResponse(responseEntity);
    return responseEntity.getBody();
  }

  protected void logRequest(String url, HttpMethod method, HttpEntity<?> requestEntity) {
    log.info("Request URL: {}, Request Method: {}, Request Headers: {}, Request Body: {}",
        url, method.name(), requestEntity.getHeaders(), requestEntity.getBody());
  }

  protected <T> void logResponse(ResponseEntity<T> responseEntity) {
    log.info("Status Code: {}, Response Body: {}",
        responseEntity.getStatusCode().value(), responseEntity.getBody());
  }

  protected MultiValueMap<String, String> getHeaders() {
    return new HttpHeaders();
  }
}
