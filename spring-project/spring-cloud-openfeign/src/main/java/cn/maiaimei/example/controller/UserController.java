package cn.maiaimei.example.controller;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.maiaimei.example.model.UserModel;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api
@Slf4j
@RestController
@RequestMapping(("/users"))
public class UserController {
    private static final Snowflake SFID = IdUtil.getSnowflake(1L, 1L);
    private static final List<UserModel> USER_MODEL_LIST = new ArrayList<>();

    static {
        UserModel userEntity = UserModel.builder().id(1550735346944315392L).nickname("超级管理员").username("admin").password("123456").build();
        USER_MODEL_LIST.add(userEntity);
    }

    @Value("${server.port}")
    private String port;

    @GetMapping("/pagingQuery")
    public List<UserModel> pagingQuery(@RequestParam Long current, @RequestParam Long size) {
        log.info("port={}", port);
        long n = (current - 1) * size;
        return USER_MODEL_LIST.stream().skip(n).limit(size).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserModel get(@PathVariable Long id) {
        log.info("port={}", port);
        return USER_MODEL_LIST.stream().filter(userEntity -> userEntity.getId().equals(id)).findFirst().orElse(null);
    }

    @PostMapping
    public UserModel create(@RequestBody UserModel userModel) {
        log.info("port={}", port);
        userModel.setId(SFID.nextId());
        USER_MODEL_LIST.add(userModel);
        return userModel;
    }

    @PutMapping
    public UserModel update(@RequestBody UserModel userModel) {
        log.info("port={}", port);
        UserModel target = USER_MODEL_LIST.stream().filter(u -> u.getId().equals(userModel.getId())).findFirst().orElse(null);
        if (target != null) {
            target.setNickname(userModel.getNickname());
            target.setUsername(userModel.getUsername());
            target.setPassword(userModel.getPassword());
        }
        return target;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("port={}", port);
        USER_MODEL_LIST.removeIf(userEntity -> userEntity.getId().equals(id));
    }
}