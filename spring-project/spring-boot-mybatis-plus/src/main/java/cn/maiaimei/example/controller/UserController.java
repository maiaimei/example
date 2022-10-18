package cn.maiaimei.example.controller;

import cn.maiaimei.example.pojo.model.UserQueryRequest;
import cn.maiaimei.example.pojo.model.UserRequest;
import cn.maiaimei.example.pojo.model.UserResponse;
import cn.maiaimei.example.service.UserService;
import cn.maiaimei.framework.beans.PagingResult;
import cn.maiaimei.framework.validation.group.ValidationGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Api(tags = "用户管理")
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "创建用户")
    @PostMapping
    public UserResponse insert(@ApiParam @Validated({ValidationGroup.CheckInOrder.class}) @RequestBody UserRequest user) {
        return userService.insert(user);
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
    @PostMapping("/pagequery")
    public PagingResult<UserResponse> pageQuery(@Min(1) @RequestParam Integer current,
                                                @Min(1) @Max(100) @RequestParam Integer size,
                                                @RequestBody(required = false) UserQueryRequest userQueryRequest) {
        return userService.pageQuery(current, size, userQueryRequest);
    }
}
