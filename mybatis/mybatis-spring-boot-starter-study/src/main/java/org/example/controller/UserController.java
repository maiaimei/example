package org.example.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.example.model.domain.User;
import org.example.model.request.UserQueryRequest;
import org.example.service.UserService;
import org.example.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/list")
  public ResponseEntity<List<User>> getUsersByConditions(@RequestBody UserQueryRequest userQueryRequest) {
    List<User> users = userService.advancedSelect(userQueryRequest);
    return ResponseEntity.ok(users);
  }

  /**
   * 根据ID获取用户
   */
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable BigDecimal id) {
    User user = userService.selectById(id);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(user);
  }

  /**
   * 创建新用户
   */
  @PostMapping
  public ResponseEntity<User> createUser(@Validated @RequestBody User user) {
    try {
      user.setId(IdGenerator.nextId());
      user.setCreateBy("system");
      user.setCreateAt(LocalDateTime.now());
      user.setUpdatedBy("system");
      user.setUpdatedAt(LocalDateTime.now());
      userService.create(user);
      return new ResponseEntity<>(user, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * 更新用户信息
   */
  @PutMapping
  public ResponseEntity<Void> updateUser(@Validated @RequestBody User user) {
    try {
      userService.update(user);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * 删除用户
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable BigDecimal id) {
    try {
      userService.deleteById(id);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

}
