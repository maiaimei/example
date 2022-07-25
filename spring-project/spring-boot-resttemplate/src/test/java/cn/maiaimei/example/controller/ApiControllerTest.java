package cn.maiaimei.example.controller;

import cn.maiaimei.example.client.HttpClient;
import cn.maiaimei.example.model.UserEntity;
import cn.maiaimei.framework.beans.Result;
import cn.maiaimei.framework.beans.ResultUtils;
import cn.maiaimei.framework.util.JSON;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO:
 */
@WebMvcTest(controllers = ApiController.class)
class ApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    HttpClient httpClient;

    @MockBean
    RestTemplate restTemplate;

    @SneakyThrows
    @Test
    void testPagingQuery() {
        ParameterizedTypeReference<Result<List<UserEntity>>> parameterizedTypeReference = new ParameterizedTypeReference<Result<List<UserEntity>>>() {
        };

        List<UserEntity> userEntities = JSON.readFileAsList("user-pagingQuery-response.json", UserEntity.class);
        ResponseEntity<Result<List<UserEntity>>> responseEntity = new ResponseEntity<Result<List<UserEntity>>>(ResultUtils.success(userEntities), HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(parameterizedTypeReference))).thenReturn(responseEntity);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/pagingQuery")
                .param("current", "1")
                .param("size", "1")
                .header("X-Auth-Token", "666");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void testGet() {
        UserEntity userEntity = UserEntity.builder().id(1550735346944315392L).nickname("超级管理员").username("admin").password("123456").build();
        Result expected = ResultUtils.success(userEntity);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(expected, HttpStatus.OK);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(ParameterizedTypeReference.class), any(Object.class))).thenReturn(responseEntity);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/1");
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(content().string(JSON.stringify(expected)));
    }
}
