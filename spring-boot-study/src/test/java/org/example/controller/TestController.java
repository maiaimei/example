package org.example.controller;

import cn.maiaimei.annotation.SkipResponseWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Test helper classes
@RestController
public class TestController {

  @SkipResponseWrapper
  @GetMapping("/skip")
  public String skippedEndpoint() {
    return "skipped";
  }

  @GetMapping("/normal")
  public String normalEndpoint() {
    return "normal";
  }
}