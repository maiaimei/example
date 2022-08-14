package cn.maiaimei.example.client;

import cn.maiaimei.example.model.UserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "spring-cloud-openfeign-server", path = "/users")
//@FeignClient(name = "spring-cloud-openfeign-server", url = "http://localhost:8080", path = "/users")
public interface UserClient {
    @GetMapping
    List<UserModel> pagingQuery(@RequestParam(name = "current") Integer current, @RequestParam(name = "size") Integer size);

    @GetMapping("/{id}")
    UserModel get(@PathVariable(name = "id") Long id);

    @PostMapping
    int create(@RequestBody UserModel user);

    @PutMapping
    int update(@RequestBody UserModel user);

    @DeleteMapping("/{id}")
    int delete(@PathVariable(name = "id") Long id);
}