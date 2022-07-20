package cn.maiaimei.example.controller;

import cn.maiaimei.example.model.User;
import cn.maiaimei.framework.util.SFID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(("/users"))
public class UserController {
    private static final List<User> users = new ArrayList<>();

    static {
        User user = User.builder().id(1L).nickname("超级管理员").username("admin").password("123456").build();
        users.add(user);
    }

    @GetMapping("/pagingQuery")
    public List<User> pagingQuery(@RequestParam Long current, @RequestParam Long size) {
        long n = (current - 1) * size;
        return users.stream().skip(n).limit(size).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        user.setId(SFID.nextId());
        users.add(user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        User target = users.stream().filter(u -> u.getId().equals(user.getId())).findFirst().orElse(null);
        if (target != null) {
            target.setNickname(user.getNickname());
            target.setUsername(user.getUsername());
            target.setPassword(user.getPassword());
        }
        return target;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}
