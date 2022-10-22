package cn.maiaimei.example.controller;

import cn.maiaimei.example.pojo.model.UserQueryRequest;
import cn.maiaimei.example.pojo.model.UserRequest;
import cn.maiaimei.example.pojo.model.UserResponse;
import cn.maiaimei.example.service.UserService;
import cn.maiaimei.framework.beans.PaginationResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api
@RequestMapping("/users")
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping
    public UserResponse insert(@RequestBody UserRequest userRequest) {
        return userService.insert(userRequest);
    }

    @PutMapping
    public UserResponse update(@RequestBody UserRequest userRequest) {
        return userService.update(userRequest);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return userService.delete(id);
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return userService.get(id);
    }

    @PostMapping("/pagequery")
    public PaginationResult<UserResponse> pageQuery(@RequestBody(required = false) UserQueryRequest userQueryRequest,
                                                    @RequestParam(required = false, defaultValue = "1") Integer current,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userService.pageQuery(userQueryRequest, current, size);
    }
}
