package cn.maiaimei.example.controller;

import cn.maiaimei.example.model.User;
import cn.maiaimei.example.model.UserPagingQueryRequest;
import cn.maiaimei.example.util.HttpClient;
import cn.maiaimei.framework.beans.Result;
import cn.maiaimei.framework.util.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(("/api"))
public class ApiController {
    private static final String baseUrl = "http://localhost:8080/users";
    private static final String PAGING_QUERY_USER_ENDPOINT = "/pagingQuery";

    @Autowired
    private HttpClient httpClient;

    @GetMapping(value = "/pageQuery", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> pageQuery(UserPagingQueryRequest request, @RequestHeader Map<String, String> headers) {
        Map<String, Object> params = JSON.toMap(request);
        Result<List<User>> result = httpClient.send(buildUrl(PAGING_QUERY_USER_ENDPOINT), headers, new ParameterizedTypeReference<Result<List<User>>>() {
        }, params);
        log.info("{}", result);
        return result.getData();
    }

    @GetMapping(value = "/pagingQuery", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result pagingQuery(UserPagingQueryRequest request, @RequestHeader Map<String, String> headers) {
        Map<String, Object> params = JSON.toMap(request);
        return httpClient.send(buildUrl(PAGING_QUERY_USER_ENDPOINT), headers, Result.class, params);
    }

    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Long id) {
        return httpClient.send(buildUrl("/" + id), new ParameterizedTypeReference<Result<User>>() {
        });
    }

    @PostMapping
    public Result<User> create(@RequestBody User user) {
        return httpClient.send(baseUrl, user, new ParameterizedTypeReference<Result<User>>() {
        });
    }

    @PutMapping
    public Result<User> update(@RequestBody User user) {
        return httpClient.exchange(baseUrl, HttpMethod.PUT, user, new ParameterizedTypeReference<Result<User>>() {
        });
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        httpClient.exchange(buildUrl("/" + id), HttpMethod.DELETE, Void.class);
    }

    private String buildUrl(String path) {
        return baseUrl + path;
    }
}
