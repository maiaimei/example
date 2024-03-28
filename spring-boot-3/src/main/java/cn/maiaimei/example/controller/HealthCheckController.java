package cn.maiaimei.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthCheckController {

  @GetMapping("/health-check")
  public String healthCheck() {
    log.info("执行目标方法");
    return "success";
  }
}
