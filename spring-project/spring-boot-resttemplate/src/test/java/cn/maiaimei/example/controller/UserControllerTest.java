package cn.maiaimei.example.controller;

import cn.maiaimei.framework.util.JSON;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @SneakyThrows
    @Test
    void testPagingQuery() {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/pagingQuery")
                .param("current", "1")
                .param("size", "1");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void testCreate() {
        String request = JSON.readFileAsString("user-create-request.json");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
//                .param("","")
//                .params(null)
//                .header("","")
//                .headers(null)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request);
        mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError());
    }
}
