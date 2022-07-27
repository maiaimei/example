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
@RequestMapping(("/api/users"))
public class ApiController {
    @Autowired
    UserClient userClient;

    @GetMapping("/pagingQuery")
    public List<UserModel> pagingQuery(@RequestParam Long current, @RequestParam Long size) {
        return userClient.pagingQuery(current, size);
    }

    @GetMapping("/{id}")
    public UserModel get(@PathVariable Long id) {
        return userClient.get(id);
    }

    @PostMapping
    public UserModel create(@RequestBody UserModel userModel) {
        return userClient.create(userModel);
    }

    @PutMapping
    public UserModel update(@RequestBody UserModel userModel) {
        return userClient.update(userModel);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userClient.delete(id);
    }
}
