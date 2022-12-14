package cn.maiaimei.example.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    public String create() {
        return "create user success";
    }

    public String update() {
        return "update user success";
    }

    public String delete() {
        return "delete user success";
    }

    public String get() {
        return "get user success";
    }

    public String pageQuery() {
        return "pageQuery user success";
    }
}
