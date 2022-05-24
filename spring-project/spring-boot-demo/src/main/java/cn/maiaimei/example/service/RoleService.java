package cn.maiaimei.example.service;

import cn.maiaimei.example.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> list(Integer page, Integer size);

    Role get(Long id);

    Role create(Role role);

    Role update(Role role);

    void delete(Long id);
}
