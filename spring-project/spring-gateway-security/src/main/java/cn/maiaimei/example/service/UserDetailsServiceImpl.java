package cn.maiaimei.example.service;

import cn.maiaimei.example.entity.UserEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(UserEntity::getUsername, username);
        UserEntity userEntity = userService.getOne(wrapper);

        if (null == userEntity) {
            throw new UsernameNotFoundException("登录账号或密码错误");
        }

        List<String> permissions = userService.getPermission(userEntity.getId());

        // 若用户存在，返回UserDetails实例，密码由SpringSecurity内部完成匹配校验
        return new User(userEntity.getUsername(), userEntity.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", permissions)));
    }
}