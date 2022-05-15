package cn.maiaimei.example.controller;

import cn.maiaimei.example.model.Role;
import cn.maiaimei.example.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    public RoleService roleService;

    @GetMapping
    public List<Role> list(@RequestParam Integer page, @RequestParam Integer size) {
        return roleService.list(page, size);
    }

    @GetMapping("/{id}")
    public Role get(@PathVariable Long id) {
        return roleService.get(id);
    }

    @PostMapping
    public Role create(@RequestBody Role role) {
        return roleService.create(role);
    }

    @PutMapping
    public Role update(@RequestBody Role role) {
        return roleService.update(role);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roleService.delete(id);
    }
}
