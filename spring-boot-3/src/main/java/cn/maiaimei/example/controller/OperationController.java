package cn.maiaimei.example.controller;

import cn.maiaimei.example.service.AddOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operation")
public class OperationController {

  @Autowired
  private AddOperation addOperation;

  @GetMapping("/add")
  public int add(@RequestParam int a, @RequestParam int b) {
    return addOperation.add(a, b);
  }
}
