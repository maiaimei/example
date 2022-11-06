package cn.maiaimei.example.controller;

import cn.maiaimei.example.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Random RANDOM = new Random();
    private static final List<User> USER_LIST = new ArrayList<>();

    static {
        USER_LIST.add(User.builder().id(1581924811813294080L).name("admin").build());
        USER_LIST.add(User.builder().id(1581924811813294081L).name("guest").build());
        USER_LIST.add(User.builder().id(1581924811813294082L).name("root").build());
        USER_LIST.add(User.builder().id(1581924811813294083L).name("sa").build());
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        log.info("get user, id is: {}", id);
        return USER_LIST.stream().filter(w -> w.getId().equals(id)).findAny().orElseGet(null);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        user.setId(Math.abs(RANDOM.nextLong()));
        USER_LIST.add(user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        User u = USER_LIST.stream().filter(w -> w.getId().equals(user.getId())).findAny().orElseGet(null);
        if (u != null) {
            u.setName(user.getName());
        }
        return user;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("delete user, id is: {}", id);
        USER_LIST.removeIf(w -> w.getId().equals(id));
    }
}
