package cn.maiaimei.example.service;

import cn.maiaimei.example.entity.UserEntity;
import cn.maiaimei.example.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, UserEntity> {
    @Resource
    private UserMapper userMapper;

    public List<String> getPermission(Long id) {
        return userMapper.getPermission(id);
    }

    public List<String> getRole(Long id) {
        return userMapper.getRole(id);
    }

    public void create() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Object principal = authentication.getPrincipal();
        log.info("{}", principal);
    }
}
