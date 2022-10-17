package cn.maiaimei.example.service.impl;

import cn.maiaimei.example.mapper.UserMapper;
import cn.maiaimei.example.pojo.entity.User;
import cn.maiaimei.example.pojo.model.UserQueryRequest;
import cn.maiaimei.example.pojo.model.UserRequest;
import cn.maiaimei.example.pojo.model.UserResponse;
import cn.maiaimei.example.service.UserService;
import cn.maiaimei.framework.beans.PagingResult;
import cn.maiaimei.framework.util.SFID;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static cn.maiaimei.framework.util.BeanUtils.copyList;
import static cn.maiaimei.framework.util.BeanUtils.copyProperties;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserResponse insert(UserRequest userRequest) {
        User user = new User();
        copyProperties(userRequest, user);
        user.setId(SFID.nextId());
        int affectedRows = userMapper.insert(user);
        if (affectedRows > 0) {
            UserResponse userResponse = new UserResponse();
            copyProperties(user, userResponse);
            return userResponse;
        }
        return null;
    }

    @Override
    public UserResponse update(UserRequest userRequest) {
        User user = new User();
        copyProperties(userRequest, user);
        int affectedRows = userMapper.update(user);
        if (affectedRows > 0) {
            UserResponse userResponse = new UserResponse();
            copyProperties(user, userResponse);
            return userResponse;
        }
        return null;
    }

    @Override
    public int delete(Long id) {
        return userMapper.delete(id);
    }

    @Override
    public UserResponse get(Long id) {
        User user = userMapper.get(id);
        UserResponse userResponse = new UserResponse();
        copyProperties(user, userResponse);
        return userResponse;
    }

    @Override
    public PagingResult<UserResponse> pageQuery(UserQueryRequest userQueryRequest, Integer current, Integer size) {
        Integer begin = (current - 1) * size;
        List<User> users = userMapper.pageQuery(userQueryRequest, begin, size);
        int total = userMapper.pageQueryCount(userQueryRequest);
        List<UserResponse> userResponseList = copyList(users, UserResponse.class);
        PagingResult<UserResponse> pagingResult = new PagingResult<>();
        pagingResult.setRecords(userResponseList);
        pagingResult.setTotal(total);
        pagingResult.setCurrent(current);
        pagingResult.setSize(size);
        return pagingResult;
    }

}
