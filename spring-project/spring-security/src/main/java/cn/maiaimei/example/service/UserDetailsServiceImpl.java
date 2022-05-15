package cn.maiaimei.example.service;

import cn.maiaimei.example.entity.UserEntity;
import cn.maiaimei.example.model.UserDetail;
import cn.maiaimei.example.model.UserInfo;
import cn.maiaimei.example.util.AuthenticationUtils;
import cn.maiaimei.framework.util.JsonUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    /**
     * 若用户存在，返回UserDetails实例，密码由SpringSecurity内部完成匹配校验，否则抛出UsernameNotFoundException异常
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(UserEntity::getUsername, username);
        UserEntity userEntity = userService.getOne(wrapper);

        if (null == userEntity) {
            throw new UsernameNotFoundException("登录账号或密码错误");
        }

        List<String> roles = Optional.ofNullable(userService.getRole(userEntity.getId())).orElse(new ArrayList<>());
        List<String> permissions = Optional.ofNullable(userService.getPermission(userEntity.getId())).orElse(new ArrayList<>());
        roles = roles.stream().map(role -> "ROLE_" + role).collect(Collectors.toList());

        List<String> authorities = new ArrayList<>();
        authorities.addAll(roles);
        authorities.addAll(permissions);

        UserInfo userInfo = UserInfo.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(StringUtils.join(authorities, ","))
                .build();
        String uid = UUID.randomUUID().toString();
        String key = AuthenticationUtils.getRedisKey4User(userEntity.getUsername(), uid);
        String val = JsonUtils.stringify(userInfo);
        // 用户信息设置5分钟失效，以防Bad credentials
        redisService.set(key, val, 2L, TimeUnit.MINUTES);

        return new UserDetail(userEntity.getUsername(), userEntity.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", authorities)),
                uid, userEntity.getSsoEnabled() == 1);
    }
}
