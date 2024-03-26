package cn.maiaimei.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthCheckController {

  private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

  @GetMapping("/health-check")
  public String healthCheck() {
    log.info("health check success -- @Slf4j");
    logger.info("health check success -- LoggerFactory.getLogger");
    return "success";
  }
}
