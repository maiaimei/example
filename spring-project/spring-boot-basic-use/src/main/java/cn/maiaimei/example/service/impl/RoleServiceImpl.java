package cn.maiaimei.example.service.impl;

import cn.maiaimei.example.model.Role;
import cn.maiaimei.example.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    private Role role = new Role(99999L, "admin");

    @Override
    public List<Role> list(Integer page, Integer size) {
        log.info("list role, page={}, size={}", page, size);
        return Arrays.asList(role);
    }

    @Override
    public Role get(Long id) {
        log.info("get role, id={}", id);
        return role;
    }

    @Override
    public Role create(Role role) {
        log.info("create role, role={}", role);
        return this.role;
    }

    @Override
    public Role update(Role role) {
        log.info("update role, role={}", role);
        return this.role;
    }

    @Override
    public void delete(Long id) {
        log.info("delete role, id={}", id);
    }
}
