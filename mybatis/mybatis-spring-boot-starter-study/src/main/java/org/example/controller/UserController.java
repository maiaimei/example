package org.example.controller;

import java.math.BigDecimal;
import java.util.List;
import org.example.model.domain.User;
import org.example.model.request.UserQueryRequest;
import org.example.service.UserService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/search")
  public ResponseEntity<List<User>> searchUsers(UserQueryRequest userQueryRequest) {
    List<User> users = userService.searchUsers(userQueryRequest);
    return ResponseEntity.ok(users);
  }

  /**
   * 获取所有用户
   */
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.findAll();
    return ResponseEntity.ok(users);
  }

  /**
   * 根据ID获取用户
   */
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable BigDecimal id) {
    User user = userService.findById(id);
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
      userService.createUser(user);
      return new ResponseEntity<>(user, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * 更新用户信息
   */
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateUser(@PathVariable BigDecimal id,
      @Validated @RequestBody User user) {
    try {
      user.setId(id);
      userService.updateUser(user);
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
      userService.deleteUser(id);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * 批量创建用户
   */
  @PostMapping("/batch")
  public ResponseEntity<Void> batchCreateUsers(@Validated @RequestBody List<User> users) {
    try {
      userService.batchInsertUsers(users, 2);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * 批量更新用户
   */
  @PutMapping("/batch")
  public ResponseEntity<Void> batchUpdateUsers(@Validated @RequestBody List<User> users) {
    try {
      userService.batchUpdateUsers(users);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * 批量更新用户（使用CASE WHEN方式）
   */
  @PutMapping("/batch/case-when")
  public ResponseEntity<Void> batchUpdateUsersByCaseWhen(
      @Validated @RequestBody List<User> users,
      @RequestParam(defaultValue = "false") boolean updatePassword) {
    try {
      userService.batchUpdateUsersByCaseWhen(users, updatePassword);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
