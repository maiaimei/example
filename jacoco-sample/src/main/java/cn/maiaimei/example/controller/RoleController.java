package cn.maiaimei.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {

  @GetMapping("/list")
  public String list() {
    return "list role";
  }

  @GetMapping("/{id}")
  public String get(@PathVariable String id) {
    return "get role " + id;
  }

}
