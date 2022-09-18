package cn.maiaimei.example.service;

import cn.maiaimei.example.mapper.UserMapper;
import cn.maiaimei.example.model.User;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper,User> {
}
