package cn.maiaimei.example.controller;

import cn.maiaimei.example.dao.UserDao;
import cn.maiaimei.example.model.UserModel;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(("/users"))
public class UserController {
    @Autowired
    private UserDao userDao;

    @GetMapping
    public List<UserModel> pagingQuery(@RequestParam(name = "current") Integer current, @RequestParam(name = "size") Integer size) {
        return userDao.selectList(current, size);
    }

    @GetMapping("/{id}")
    public UserModel get(@PathVariable Long id) {
        return userDao.selectOne(id);
    }

    @PostMapping
    public int create(@RequestBody UserModel user) {
        return userDao.create(user);
    }

    @PutMapping
    public int update(@RequestBody UserModel user) {
        return userDao.update(user);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return userDao.delete(id);
    }
}