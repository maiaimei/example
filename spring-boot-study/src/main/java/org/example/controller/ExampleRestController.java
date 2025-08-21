package org.example.controller;

import org.example.model.UserDTO;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExampleRestController {

  @PutMapping("/form-content/update/username")
  public String update(@RequestParam String username) {
    return "Updated username: " + username;
  }

  // 如果需要接收表单对象
  @PutMapping("/form-content/update/user")
  public String updateUser(@ModelAttribute UserDTO userDTO) {
    return "Updated user: " + userDTO.getName();
  }

}
