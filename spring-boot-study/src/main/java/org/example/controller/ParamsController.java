package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import cn.maiaimei.model.User;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/params")
public class ParamsController {

  // 路径变量 - @PathVariable
  @GetMapping("/path-variable/{id}/{name}")
  public String pathVariableDemo(@PathVariable Long id, @PathVariable String name) {
    return "Path Variables - id: %d, name: %s".formatted(id, name);
  }

  // 请求参数 - @RequestParam
  @GetMapping("/request-param")
  public String requestParamDemo1(
      @RequestParam String name,
      @RequestParam(required = false, defaultValue = "18") Integer age) {
    return "Request Parameters - name: %s, age: %d".formatted(name, age);
  }

  // 请求参数 - @RequestParam - 使用 Map 接收所有参数
  @PostMapping("/request-param/map")
  public String requestParamDemo2(@RequestParam Map<String, String> params) {
    return "Request Parameters: %s".formatted(params);
  }

  // 请求参数 - @RequestParam - 使用 MultiValueMap 接收多值参数
  @PostMapping("/request-param/multi-value-map")
  public String requestParamDemo3(@RequestParam MultiValueMap<String, String> params) {
    return "Request Parameters: %s".formatted(params);
  }

  // 请求体 - @RequestBody
  @PostMapping("/request-body")
  public String requestBodyDemo(@RequestBody User user) {
    return "Request Body - user: %s".formatted(user.toString());
  }

  // 请求头 - @RequestHeader
  @GetMapping("/request-header")
  public String requestHeaderDemo(
      @RequestHeader("User-Agent") String userAgent,
      @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
    return "Headers - User-Agent: %s, Accept-Language: %s".formatted(userAgent, acceptLanguage);
  }

  // Cookie值 - @CookieValue
  @GetMapping("/cookie-value")
  public String cookieValueDemo(
      @CookieValue(value = "sessionId", required = false) String sessionId) {
    return "Cookie Value - sessionId: %s".formatted(sessionId);
  }

  // 表单数据 - @ModelAttribute
  @PostMapping("/model-attribute")
  public String modelAttributeDemo(@ModelAttribute User user) {
    return "Form Data - %s".formatted(user.toString());
  }

  // 直接使用对象接收参数
  @PostMapping("/object")
  public String objectDemo(User user) {
    return "Form Data - %s".formatted(user.toString());
  }

  // 组合参数 - 表单数据和路径变量
  @PostMapping("/mixed/{id}")
  public String mixedParamsDemo(
      @PathVariable Long id,
      @RequestParam String name,
      @RequestHeader("User-Agent") String userAgent) {
    return String.format("Mixed - id: %d, name: %s, User-Agent: %s", id, name, userAgent);
  }

  // 使用 HttpServletRequest 接收参数
  @PostMapping("/http-servlet-request")
  public String httpServletRequestDemo(HttpServletRequest request) {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    return String.format("Form6 - username: %s, password: %s", username, password);
  }
}
