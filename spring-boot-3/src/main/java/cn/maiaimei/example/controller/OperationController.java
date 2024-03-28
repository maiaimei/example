package cn.maiaimei.example.controller;

import cn.maiaimei.example.service.AddOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/operation")
public class OperationController {

  @Autowired
  private AddOperation addOperation;

  @GetMapping("/add")
  public int add(@RequestParam int a, @RequestParam int b) {
    log.info("执行目标方法");
    return addOperation.add(a, b);
  }
}
