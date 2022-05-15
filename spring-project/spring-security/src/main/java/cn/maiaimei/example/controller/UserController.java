package cn.maiaimei.example.controller;

import cn.maiaimei.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('user:create')")
    @GetMapping("/create")
    public String create() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Object principal = authentication.getPrincipal();
        log.info("{}", principal);
        userService.create();
        return "create user";
    }

    @PreAuthorize("hasAuthority('user:update')")
    @GetMapping("/update")
    public String update() {
        return "update user";
    }

    @PreAuthorize("hasAuthority('user:delete')")
    @GetMapping("/delete")
    public String delete() {
        return "delete user";
    }

    @PreAuthorize("hasAuthority('user:get')")
    @GetMapping("/get")
    public String get() {
        return "get user";
    }

    @PreAuthorize("hasAuthority('user:pageQuery')")
    @GetMapping("/pageQuery")
    public String pageQuery() {
        return "pageQuery user";
    }
}
