package cn.maiaimei.example.controller;

import cn.maiaimei.example.model.UserRequest;
import cn.maiaimei.example.model.UserResponse;
import cn.maiaimei.example.service.UserService;
import cn.maiaimei.framework.spring.boot.validation.ValidationGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Api(tags = "用户管理")
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "创建用户")
    @PostMapping
    public UserResponse create(@ApiParam @Validated({ValidationGroup.CheckInOrder.class}) @RequestBody UserRequest user) {
        return userService.create(user);
    }

    @ApiOperation(value = "修改用户")
    @PutMapping
    public UserResponse update(@ApiParam @Validated({ValidationGroup.Update.class, ValidationGroup.CheckInOrder.class}) @RequestBody UserRequest user) {
        return userService.update(user);
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return userService.delete(id);
    }

    @ApiOperation(value = "获取指定用户")
    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return userService.get(id);
    }

    @ApiOperation(value = "分页查询用户")
    @GetMapping("/pageQuery")
    public List<UserResponse> pageQuery(@Min(1) @RequestParam Integer pageIndex,
                                        @Min(1) @Max(100) @RequestParam Integer pageSize) {
        return userService.pageQuery(pageIndex, pageSize);
    }

    @ApiOperation(value = "查询全部用户")
    @GetMapping("/listAll")
    public List<UserResponse> listAll() {
        return userService.listAll();
    }
}
