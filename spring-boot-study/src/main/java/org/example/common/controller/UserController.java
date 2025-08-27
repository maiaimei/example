package org.example.common.controller;

import cn.maiaimei.model.ApiRequest;
import cn.maiaimei.util.IdGenerator;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.example.common.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

  private static final List<User> users = new ArrayList<>();

  @GetMapping("/{id}")
  public User get(BigDecimal id) {
    return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
  }

  @PostMapping
  public User create(@RequestBody @Valid ApiRequest<User> apiRequest) {
    final User user = apiRequest.getData();
    user.setId(IdGenerator.nextId());
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(user.getCreatedAt());
    users.add(user);
    return user;
  }

  @PutMapping
  public User update(@RequestBody ApiRequest<User> apiRequest) {
    final User user = apiRequest.getData();
    final User existingUser = users.stream().filter(u -> u.getId().equals(user.getId())).findFirst().orElse(null);
    if (Objects.isNull(existingUser)) {
      throw new RuntimeException("User not found");
    }
    existingUser.setName(user.getName());
    existingUser.setEmail(user.getEmail());
    existingUser.setUpdatedAt(LocalDateTime.now());
    return existingUser;
  }

  @PatchMapping
  public User partialUpdate(@RequestBody ApiRequest<User> apiRequest) {
    final User user = apiRequest.getData();
    final User existingUser = users.stream().filter(u -> u.getId().equals(user.getId())).findFirst().orElse(null);
    if (Objects.isNull(existingUser)) {
      throw new RuntimeException("User not found");
    }
    existingUser.setEmail(user.getEmail());
    existingUser.setUpdatedAt(LocalDateTime.now());
    return existingUser;
  }

  @DeleteMapping("/{id}")
  public String delete(BigDecimal id) {
    return "User delete successfully";
  }

}
