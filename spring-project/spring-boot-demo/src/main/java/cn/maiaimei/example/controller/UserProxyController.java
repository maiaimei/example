package cn.maiaimei.example.controller;

import cn.maiaimei.example.client.HttpClient;
import cn.maiaimei.example.model.User;
import cn.maiaimei.framework.beans.Result;
import cn.maiaimei.framework.validation.group.ValidationGroup;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Api(tags = "用户代理")
@Validated
@RestController
@RequestMapping("/userproxy")
public class UserProxyController {
    private String baseUrl = "http://localhost:";

    @Value("${server.port}")
    private String port;

    @Autowired
    private HttpClient httpClient;

    @PostConstruct
    public void init() {
        baseUrl = baseUrl + port + "/users";
    }

    @ApiOperation(value = "创建用户")
    @PostMapping
    public Result create(@Validated({ValidationGroup.CheckInOrder.class}) @RequestBody User user) {
        return httpClient.post(baseUrl, user, Result.class);
    }

    @ApiOperation(value = "修改用户")
    @PutMapping
    public Result update(@Validated({ValidationGroup.Update.class, ValidationGroup.CheckInOrder.class}) @RequestBody User user) {
        return httpClient.exchange(baseUrl, HttpMethod.PUT, user, Result.class);
    }

    @ApiOperation(value = "修改用户")
    @PutMapping("/update4form")
    public Result update4form(@Validated({ValidationGroup.Update.class, ValidationGroup.CheckInOrder.class}) User user) {
        return httpClient.exchange(baseUrl, HttpMethod.PUT, user, Result.class);
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{userId}")
    public void delete(@ApiParam(name = "userId", value = "用户ID", required = true, example = "1512076060877955072") @PathVariable Long userId) {
        httpClient.exchange(baseUrl + "/" + userId, HttpMethod.DELETE, Void.class);
    }

    @ApiOperation(value = "获取用户")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, example = "1512076060877955072")
    @GetMapping("/{userId}")
    public Result get(@PathVariable Long userId) {
        return httpClient.exchange(baseUrl + "/" + userId, HttpMethod.GET, Result.class);
    }

    @ApiOperation(value = "分页查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小")
    })
    @GetMapping
    public List<User> pageQuery(@Min(1) @RequestParam(required = false) Integer pageIndex,
                                @Min(1) @Max(100) @RequestParam(required = false) Integer pageSize) {
        // TODO
        return null;
    }
}
