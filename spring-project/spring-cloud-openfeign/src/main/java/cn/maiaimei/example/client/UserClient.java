package cn.maiaimei.example.client;

import cn.maiaimei.example.model.UserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "users-service", path = "/users")
// @FeignClient(name = "users-service", url = "http://localhost:8080", path = "/users")
public interface UserClient {
    @GetMapping("/pagingQuery")
    List<UserModel> pagingQuery(@RequestParam(name = "current") Long current, @RequestParam(name = "size") Long size);

    @GetMapping("/{id}")
    UserModel get(@PathVariable(name = "id") Long id);

    @PostMapping
    UserModel create(@RequestBody UserModel userEntity);

    @PutMapping
    UserModel update(@RequestBody UserModel userEntity);

    @DeleteMapping("/{id}")
    void delete(@PathVariable(name = "id") Long id);
}
