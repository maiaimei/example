package org.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.example.controller.usercenter.UserController;
import org.example.model.domain.User;
import org.example.model.request.UserQueryRequest;
import org.example.service.usercenter.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  private User testUser;
  private List<User> userList;
  private UserQueryRequest queryRequest;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setId(new BigDecimal("1"));
    testUser.setUsername("Test User");
    testUser.setEmail("test@example.com");
    testUser.setCreateAt(LocalDateTime.now());
    testUser.setCreateBy("system");
    testUser.setUpdatedAt(LocalDateTime.now());
    testUser.setUpdatedBy("system");

    userList = List.of(testUser);

    queryRequest = new UserQueryRequest();
    queryRequest.setUsername("Test");
  }

  @Test
  void getUsers1_ShouldReturnUserList() throws Exception {
    when(userService.getUsers1(any(UserQueryRequest.class))).thenReturn(userList);

    mockMvc.perform(post("/users/list1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(queryRequest)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id").value("1"))
        .andExpect(jsonPath("$[0].username").value("Test User"));
  }

  @Test
  void getUsers2_ShouldReturnUserList() throws Exception {
    when(userService.getUsers2(any(UserQueryRequest.class))).thenReturn(userList);

    mockMvc.perform(post("/users/list2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(queryRequest)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id").value("1"));
  }

  @Test
  void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
    when(userService.selectById(any(BigDecimal.class))).thenReturn(testUser);

    mockMvc.perform(get("/users/{id}", "1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.username").value("Test User"));
  }

  @Test
  void getUserById_WhenUserDoesNotExist_ShouldReturn404() throws Exception {
    when(userService.selectById(any(BigDecimal.class))).thenReturn(null);

    mockMvc.perform(get("/users/{id}", "999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void createUser_WithValidData_ShouldReturnCreatedUser() throws Exception {
    doNothing().when(userService).create(any(User.class));

    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testUser)))
        .andExpect(status().isCreated());
  }

  @Test
  void createUser_WithInvalidData_ShouldReturn400() throws Exception {
    doThrow(new IllegalArgumentException()).when(userService).create(any(User.class));

    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testUser)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateUser_WithValidData_ShouldReturn200() throws Exception {
    doNothing().when(userService).update(any(User.class));

    mockMvc.perform(put("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testUser)))
        .andExpect(status().isOk());
  }

//  @Test
//  void updateUser_WithInvalidData_ShouldReturn400() throws Exception {
//    doNothing().when(userService).update(any(User.class));
//    testUser.setEmail("invalid-email");
//
//    mockMvc.perform(put("/users")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(testUser)))
//        .andExpect(status().isBadRequest());
//  }

  @Test
  void deleteUser_WithValidId_ShouldReturn200() throws Exception {
    doNothing().when(userService).deleteById(any(BigDecimal.class));

    mockMvc.perform(delete("/users/{id}", "1"))
        .andExpect(status().isOk());
  }

  @Test
  void deleteUser_WithInvalidId_ShouldReturn400() throws Exception {
    doNothing().when(userService).deleteById(any(BigDecimal.class));

    mockMvc.perform(delete("/users/{id}", "invalid-id"))
        .andExpect(status().isBadRequest());
  }
}
