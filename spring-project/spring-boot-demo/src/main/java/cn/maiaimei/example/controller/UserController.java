package cn.maiaimei.example.controller;

import cn.maiaimei.example.model.User;
import cn.maiaimei.framework.exception.BusinessException;
import cn.maiaimei.framework.util.SFID;
import cn.maiaimei.framework.validation.group.ValidationGroup;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = "Swagger API Demo - 用户管理")
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private List<User> users = new ArrayList<>();

    @ApiOperation(value = "创建用户")
    @PostMapping
    public User create(@Validated({ValidationGroup.CheckInOrder.class}) @RequestBody User user) {
        if (user.getUserId() == null) {
            user.setUserId(SFID.nextId());
        }
        users.add(user);
        return user;
    }

    @ApiOperation(value = "修改用户")
    @PutMapping
    public User update(@Validated({ValidationGroup.Update.class, ValidationGroup.CheckInOrder.class}) @RequestBody User user) {
        return updateUser(user);
    }

    @ApiOperation(value = "修改用户")
    @PutMapping("/update4form")
    public User update4form(@Validated({ValidationGroup.Update.class, ValidationGroup.CheckInOrder.class}) User user) {
        return updateUser(user);
    }

    private User updateUser(User user) {
        Optional<User> optional = users.stream().filter(item -> item.getUserId().equals(user.getUserId())).findAny();
        if (optional.isPresent()) {
            User usr = optional.get();
            usr.setNickname(user.getNickname());
            usr.setUsername(user.getUsername());
            usr.setPassword(user.getPassword());
            return usr;
        }
        throw new BusinessException("用户不存在");
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{userId}")
    public void delete(@ApiParam(name = "userId", value = "用户ID", required = true, example = "1512076060877955072") @PathVariable Long userId) {
        List<User> collect = users.stream().filter(item -> !item.getUserId().equals(userId)).collect(Collectors.toList());
        if (users.size() == collect.size()) {
            throw new BusinessException("用户不存在");
        }
        users = collect;
    }

    @ApiOperation(value = "获取用户")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, example = "1512076060877955072")
    @GetMapping("/{userId}")
    public User get(@PathVariable Long userId) {
        Optional<User> optional = users.stream().filter(item -> item.getUserId().equals(userId)).findAny();
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new BusinessException("用户不存在");
    }

    @ApiOperation(value = "分页查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小")
    })
    @GetMapping
    public List<User> pageQuery(@Min(1) @RequestParam(required = false) Integer pageIndex,
                                @Min(1) @Max(100) @RequestParam(required = false) Integer pageSize) {
        return users;
    }
}
