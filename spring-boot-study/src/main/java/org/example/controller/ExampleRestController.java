package org.example.controller;

import org.example.model.request.ApiRequest;
import org.example.model.request.dto.UserDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleRestController {

  @GetMapping("/method/get")
  public String get(){
    return "GET：读取（Read）";
  }

  @PostMapping("/method/post")
  public String post(){
    return "POST：新建（Create）";
  }

  @PutMapping("/method/put")
  public String put(){
    return "PUT：更新（Update）";
  }

  @PatchMapping("/method/patch")
  public String patch(){
    return "PATCH：更新（Update），通常是部分更新";
  }

  @DeleteMapping("/method/delete")
  public String delete(){
    return "DELETE：删除（Delete）";
  }

  @PutMapping("/form-content/update/username")
  public String update(@RequestParam String username) {
    return "Updated username: " + username;
  }

  // 如果需要接收表单对象
  @PutMapping("/form-content/update/user")
  public String updateUser(@ModelAttribute UserDTO userDTO) {
    return "Updated user: " + userDTO.getUsername();
  }

}
