package cn.maiaimei.example.controller;

import cn.maiaimei.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/list")
  public String list() {
    return userService.list();
  }

  @GetMapping("/{id}")
  public String get(@PathVariable String id) {
    return "get user " + id;
  }

}
