package org.example.controller;

import org.example.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/params")
public class ParamsDemoController {

  // 路径变量 - @PathVariable
  @GetMapping("/path/{id}/{name}")
  public String pathVariableDemo(@PathVariable Long id, @PathVariable String name) {
    return "Path Variables - id: %d, name: %s".formatted(id, name);
  }

  // 请求参数 - @RequestParam
  @GetMapping("/request")
  public String requestParamDemo(
      @RequestParam String name,
      @RequestParam(required = false, defaultValue = "18") Integer age) {
    return "Request Parameters - name: %s, age: %d".formatted(name, age);
  }

  // 请求体 - @RequestBody
  @PostMapping("/body")
  public String requestBodyDemo(@RequestBody User user) {
    return "Request Body - user: %s".formatted(user.toString());
  }

  // 请求头 - @RequestHeader
  @GetMapping("/header")
  public String requestHeaderDemo(
      @RequestHeader("User-Agent") String userAgent,
      @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
    return "Headers - User-Agent: %s, Accept-Language: %s".formatted(userAgent, acceptLanguage);
  }

  // Cookie值 - @CookieValue
  @GetMapping("/cookie")
  public String cookieValueDemo(
      @CookieValue(value = "sessionId", required = false) String sessionId) {
    return "Cookie Value - sessionId: %s".formatted(sessionId);
  }

  // 表单数据 - @ModelAttribute
  @PostMapping("/form")
  public String formDataDemo(@ModelAttribute User user) {
    return "Form Data - %s".formatted(user.toString());
  }

  // 组合参数 - 表单数据和路径变量
  @PostMapping("/mixed/{id}")
  public String mixedParamsDemo(
      @PathVariable Long id,
      @RequestParam String name,
      @RequestHeader("User-Agent") String userAgent) {
    return String.format("Mixed - id: %d, name: %s, User-Agent: %s",
        id, name, userAgent);
  }
}
