package cn.maiaimei.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping(value = {
            "", "/", "index", "main"
    })
    public String index() {
        return "index";
    }
}
