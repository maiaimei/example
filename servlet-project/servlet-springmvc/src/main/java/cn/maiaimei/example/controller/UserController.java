package cn.maiaimei.example.controller;

import cn.maiaimei.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String create() {
        return userService.create();
    }

    @PutMapping
    public String update() {
        return userService.update();
    }

    @DeleteMapping
    public String delete() {
        return userService.delete();
    }

    @GetMapping
    public String get() {
        return userService.get();
    }

    @GetMapping("/pageQuery")
    public String pageQuery() {
        return userService.pageQuery();
    }
}
