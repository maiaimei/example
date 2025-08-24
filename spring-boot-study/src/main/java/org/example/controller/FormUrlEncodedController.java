package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.Data;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/form")
public class FormUrlEncodedController {

  // 使用 @RequestParam 接收单个参数
  @PostMapping("/form1")
  public String handleForm1(
      @RequestParam String username,
      @RequestParam String password,
      @RequestParam(required = false) String email) {
    return String.format("Form1 - username: %s, password: %s, email: %s",
        username, password, email);
  }

  // 使用 @ModelAttribute 接收参数对象
  @PostMapping("/form2")
  public String handleForm2(@ModelAttribute UserForm userForm) {
    return "Form2 - " + userForm.toString();
  }

  // 直接使用对象接收参数
  @PostMapping("/form3")
  public String handleForm3(UserForm userForm) {
    return "Form3 - " + userForm.toString();
  }

  // 使用 Map 接收所有参数
  @PostMapping("/form4")
  public String handleForm4(@RequestParam Map<String, String> params) {
    return "Form4 - All parameters: " + params;
  }

  // 使用 MultiValueMap 接收多值参数
  @PostMapping("/form5")
  public String handleForm5(@RequestParam MultiValueMap<String, String> params) {
    return "Form5 - All parameters: " + params;
  }

  // 使用 HttpServletRequest 接收参数
  @PostMapping("/form6")
  public String handleForm6(HttpServletRequest request) {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    return String.format("Form6 - username: %s, password: %s",
        username, password);
  }
}

@Data
class UserForm {

  private String username;
  private String password;
  private String email;
}
