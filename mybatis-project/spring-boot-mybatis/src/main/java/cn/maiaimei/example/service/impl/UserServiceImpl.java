package cn.maiaimei.example.service.impl;

import cn.maiaimei.example.mapper.UserMapper;
import cn.maiaimei.example.pojo.entity.User;
import cn.maiaimei.example.pojo.model.UserQueryRequest;
import cn.maiaimei.example.pojo.model.UserRequest;
import cn.maiaimei.example.pojo.model.UserResponse;
import cn.maiaimei.example.service.UserService;
import cn.maiaimei.framework.beans.PaginationResult;
import cn.maiaimei.framework.util.SFID;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
    public PaginationResult<UserResponse> pageQuery(UserQueryRequest userQueryRequest, Integer current, Integer size) {
        Page<User> page = PageHelper.startPage(current, size);
        List<User> users = userMapper.list(userQueryRequest);
        List<UserResponse> userResponseList = copyList(users, UserResponse.class);
        PaginationResult<UserResponse> pagingResult = new PaginationResult<>();
        pagingResult.setRecords(userResponseList);
        pagingResult.setTotal(page.getTotal());
        pagingResult.setCurrent(page.getPageNum());
        pagingResult.setSize(page.getPageSize());
        return pagingResult;
    }

}
