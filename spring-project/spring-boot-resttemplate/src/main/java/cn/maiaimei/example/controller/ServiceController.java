package cn.maiaimei.example.controller;

import cn.maiaimei.example.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(("/service"))
public class ServiceController {
    private User user = User.builder().id(1L).nickname("超级管理员").username("admin").password("123456").build();

    @GetMapping("/pagingQuery")
    public List<User> pagingQuery(@RequestParam Long current, @RequestParam Long size) {
        log.info("current={}, size={}", current, size);
        return Arrays.asList(user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        log.info("id={}", id);
        return user;
    }
}
