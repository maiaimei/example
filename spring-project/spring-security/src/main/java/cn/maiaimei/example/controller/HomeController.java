package cn.maiaimei.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @PreAuthorize("hasAuthority('index:access')")
    @RequestMapping("/toMain")
    public String toMain() {
        return "main";
    }

    @PreAuthorize("hasAuthority('index:access')")
    @GetMapping(value = {"", "/", "index"})
    public String index() {
        return "index";
    }
}
