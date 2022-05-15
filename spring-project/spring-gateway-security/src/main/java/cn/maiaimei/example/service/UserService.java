package cn.maiaimei.example.service;

import cn.maiaimei.example.entity.UserEntity;
import cn.maiaimei.example.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, UserEntity> {
    @Resource
    private UserMapper userMapper;

    public List<String> getPermission(Long id) {
        return userMapper.getPermission(id);
    }
}
