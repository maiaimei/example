package cn.maiaimei.example.controller;

import cn.maiaimei.example.dao.UserDao;
import cn.maiaimei.example.model.UserModel;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserDao userDao;

    @PostMapping
    public int create(@RequestBody UserModel userModel) {
        return userDao.create(userModel);
    }

    @PutMapping
    public int update(@RequestBody UserModel userModel) {
        return userDao.update(userModel);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return userDao.delete(id);
    }

    @PostMapping("/batchCreate")
    public List<UserModel> batchCreate(@RequestBody List<UserModel> userModels) {
        return userDao.batchCreate(userModels);
    }

    @GetMapping("/{id}")
    public UserModel selectOne(@PathVariable Long id) {
        return userDao.selectOne(id);
    }

    @GetMapping("/selectList")
    public List<UserModel> selectList() {
        return userDao.selectList();
    }

    @GetMapping("/queryList")
    public List<UserModel> queryList() {
        return userDao.queryList();
    }
}
