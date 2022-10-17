package cn.maiaimei.example.controller;

import cn.maiaimei.example.TestBase;
import cn.maiaimei.example.pojo.model.UserResponse;
import cn.maiaimei.example.service.UserService;
import cn.maiaimei.framework.beans.PagingResult;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest extends TestBase {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @SneakyThrows
    @Test
    void testInsert() {
        String request = readFileAsString(INSERT_REQUEST);
        UserResponse response = readFileAsObject(INSERT_RESPONSE, UserResponse.class);
        when(userService.insert(any())).thenReturn(response);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
                .headers(getHttpHeaders());
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @SneakyThrows
    @Test
    void testUpdate() {
        String request = readFileAsString(USER);
        UserResponse response = readFileAsObject(USER, UserResponse.class);
        when(userService.update(any())).thenReturn(response);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
                .headers(getHttpHeaders());
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @SneakyThrows
    @Test
    void testDelete() {
        when(userService.delete(anyLong())).thenReturn(1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users/1581965723515883520")
                .headers(getHttpHeaders());
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @SneakyThrows
    @Test
    void testGet() {
        UserResponse response = readFileAsObject(USER, UserResponse.class);
        when(userService.get(anyLong())).thenReturn(response);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/1581965723515883520")
                .headers(getHttpHeaders());
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @SneakyThrows
    @Test
    void testPageQuery() {
        String request = readFileAsString(PAGE_QUERY_REQUEST);
        PagingResult<UserResponse> response = readFileAsObject(PAGE_QUERY_RESPONSE, new TypeReference<PagingResult<UserResponse>>() {
        });
        when(userService.pageQuery(any(), anyInt(), anyInt())).thenReturn(response);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/pagequery")
                .param("current", "1")
                .param("size", "3")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
                .headers(getHttpHeaders());
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print());
    }
}
