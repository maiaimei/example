package cn.maiaimei.example.controller;

import cn.maiaimei.example.model.UserEntity;
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
    private static final List<UserEntity> USER_ENTITIES = new ArrayList<>();

    static {
        UserEntity userEntity = UserEntity.builder().id(1550735346944315392L).nickname("超级管理员").username("admin").password("123456").build();
        USER_ENTITIES.add(userEntity);
    }

    @GetMapping("/pagingQuery")
    public List<UserEntity> pagingQuery(@RequestParam Long current, @RequestParam Long size) {
        long n = (current - 1) * size;
        return USER_ENTITIES.stream().skip(n).limit(size).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserEntity get(@PathVariable Long id) {
        return USER_ENTITIES.stream().filter(userEntity -> userEntity.getId().equals(id)).findFirst().orElse(null);
    }

    @PostMapping
    public UserEntity create(@RequestBody UserEntity userEntity) {
        userEntity.setId(SFID.nextId());
        USER_ENTITIES.add(userEntity);
        return userEntity;
    }

    @PutMapping
    public UserEntity update(@RequestBody UserEntity userEntity) {
        UserEntity target = USER_ENTITIES.stream().filter(u -> u.getId().equals(userEntity.getId())).findFirst().orElse(null);
        if (target != null) {
            target.setNickname(userEntity.getNickname());
            target.setUsername(userEntity.getUsername());
            target.setPassword(userEntity.getPassword());
        }
        return target;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        USER_ENTITIES.removeIf(userEntity -> userEntity.getId().equals(id));
    }
}
