package cn.maiaimei.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * By default, @SpringBootTest does not start the server but instead sets up a mock environment for
 * testing web endpoints.
 * <p>
 * With Spring MVC, we can query our web endpoints using MockMvc or WebTestClient.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class HealthCheckControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  public void testHealthCheck() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/health-check"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("success"));
  }
}
