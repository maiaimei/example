package org.example.controller;

import org.example.annotation.SkipResponseWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

  @SkipResponseWrapper
  @GetMapping("/health-check")
  public String healthCheck() {
    return "ok";
  }
}
