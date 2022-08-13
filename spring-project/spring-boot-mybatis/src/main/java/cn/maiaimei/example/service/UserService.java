package cn.maiaimei.example.service;

import cn.maiaimei.example.entity.User;
import cn.maiaimei.example.mapper.UserMapper;
import cn.maiaimei.example.model.UserRequest;
import cn.maiaimei.example.model.UserResponse;
import cn.maiaimei.framework.exception.BusinessException;
import cn.maiaimei.framework.util.BeanUtils;
import cn.maiaimei.framework.util.SFID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public UserResponse create(UserRequest userRequest) {
        User user = BeanUtils.copyProperties(userRequest, User.class);
        user.setId(SFID.nextId());
        int cnt = userMapper.create(user);
        if (cnt > 0) {
            return BeanUtils.copyProperties(user, UserResponse.class);
        }
        throw new BusinessException("创建用户失败");
    }

    public UserResponse update(UserRequest userRequest) {
        User user = BeanUtils.copyProperties(userRequest, User.class);
        int cnt = userMapper.update(user);
        if (cnt > 0) {
            return BeanUtils.copyProperties(user, UserResponse.class);
        }
        throw new BusinessException("修改用户失败");
    }

    public String delete(Long id) {
        int cnt = userMapper.delete(id);
        return cnt > 0 ? "删除用户成功" : "删除用户失败";
    }

    public UserResponse get(Long id) {
        User user = userMapper.get(id);
        if (null != user) {
            return BeanUtils.copyProperties(user, UserResponse.class);
        }
        throw new BusinessException("获取用户失败");
    }

    public List<UserResponse> listAll() {
        List<User> users = userMapper.listAll();
        return BeanUtils.copyList(users, UserResponse.class);
    }

    public List<UserResponse> pageQuery(Integer pageIndex,
                                        Integer pageSize) {
        List<User> users = userMapper.pageQuery((pageIndex - 1) * pageSize, pageSize);
        return BeanUtils.copyList(users, UserResponse.class);
    }
}
