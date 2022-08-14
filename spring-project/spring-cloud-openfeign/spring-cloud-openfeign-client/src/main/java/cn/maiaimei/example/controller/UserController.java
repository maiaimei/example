package cn.maiaimei.example.controller;

import cn.maiaimei.example.client.UserClient;
import cn.maiaimei.example.model.UserModel;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@Slf4j
@RestController
@RequestMapping(("/users"))
public class UserController {
    @Autowired
    UserClient userClient;

    @GetMapping
    public List<UserModel> pagingQuery(@RequestParam Integer current, @RequestParam Integer size) {
        return userClient.pagingQuery(current, size);
    }

    @GetMapping("/{id}")
    public UserModel get(@PathVariable Long id) {
        return userClient.get(id);
    }

    @PostMapping
    public int create(@RequestBody UserModel user) {
        return userClient.create(user);
    }

    @PutMapping
    public int update(@RequestBody UserModel user) {
        return userClient.update(user);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return userClient.delete(id);
    }
}
