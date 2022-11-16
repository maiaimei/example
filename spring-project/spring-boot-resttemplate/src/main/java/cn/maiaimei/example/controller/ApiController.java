package cn.maiaimei.example.controller;

import cn.maiaimei.example.client.HttpClient;
import cn.maiaimei.example.model.UserEntity;
import cn.maiaimei.example.model.UserPagingQueryRequest;
import cn.maiaimei.framework.beans.Result;
import cn.maiaimei.framework.util.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api
@RestController
@RequestMapping(("/api/users"))
public class ApiController {
    private static final String BASE_URL = "http://localhost:8080/users";
    private static final String PAGING_QUERY_USER_ENDPOINT = "/pagingQuery";

    @Autowired
    private HttpClient httpClient;

    @GetMapping(value = "/pagingQuery", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserEntity> pagingQuery(UserPagingQueryRequest request, @RequestHeader(required = false) Map<String, String> headers) {
        Map<String, Object> params = JSON.parse(JSON.stringify(request), new TypeReference<Map<String, Object>>() {
        });
        Result<List<UserEntity>> result = httpClient.send(buildUrl(PAGING_QUERY_USER_ENDPOINT), headers, new ParameterizedTypeReference<Result<List<UserEntity>>>() {
        }, params);
        return result.getData();
    }

    @GetMapping("/{id}")
    public Result<UserEntity> get(@PathVariable Long id) {
        return httpClient.get(buildUrl("/" + id), new ParameterizedTypeReference<Result<UserEntity>>() {
        });
    }

    @PostMapping
    public Result<UserEntity> create(@RequestBody UserEntity userEntity) {
        return httpClient.post(BASE_URL, userEntity, new ParameterizedTypeReference<Result<UserEntity>>() {
        });
    }

    @PutMapping
    public Result<UserEntity> update(@RequestBody UserEntity userEntity) {
        return httpClient.put(BASE_URL, userEntity, new ParameterizedTypeReference<Result<UserEntity>>() {
        });
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        httpClient.delete(buildUrl("/" + id));
    }

    private String buildUrl(String path) {
        return BASE_URL + path;
    }
}
