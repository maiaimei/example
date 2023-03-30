package cn.maiaimei.example.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/example")
public class ExampleController {

    @GetMapping("/sit/method1/{id}")
    public String method1(@PathVariable Long id) {
        return "sit: " + id;
    }

    @GetMapping("/uat/method1/{id}")
    public String method2(@PathVariable Long id) {
        return "uat: " + id;
    }

}
