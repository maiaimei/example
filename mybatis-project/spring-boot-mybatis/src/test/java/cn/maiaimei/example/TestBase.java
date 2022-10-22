package cn.maiaimei.example;

import cn.maiaimei.framework.util.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestBase {
    protected static final String USER = "user.json";
    protected static final String INSERT_REQUEST = "insertRequest.json";
    protected static final String INSERT_RESPONSE = "insertResponse.json";
    protected static final String PAGE_QUERY_REQUEST = "pageQueryRequest.json";
    protected static final String PAGE_QUERY_RESPONSE = "pageQueryResponse.json";
    protected static final String PAGE_QUERY_RESULT = "pageQueryResult.json";

    protected <T> T readFileAsObject(String path, TypeReference<T> valueTypeRef) {
        return JSON.readFileAsObject(path, valueTypeRef);
    }

    protected <T> T readFileAsObject(String path, Class<T> clazz) {
        return JSON.readFileAsObject(path, clazz);
    }

    protected String readFileAsString(String path) {
        return JSON.readFileAsString(path);
    }

    protected Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-auth-token", "test");
        return headers;
    }

    protected HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> headers = getHeaders();
        headers.forEach((key, value) -> httpHeaders.put(key, Collections.singletonList(value)));
        return httpHeaders;
    }
}
