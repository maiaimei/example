package cn.maiaimei.example.service;

import cn.maiaimei.example.mapper.UserMapper;
import cn.maiaimei.example.pojo.entity.User;
import cn.maiaimei.example.pojo.model.UserQueryRequest;
import cn.maiaimei.example.pojo.model.UserRequest;
import cn.maiaimei.example.pojo.model.UserResponse;
import cn.maiaimei.framework.beans.PagingResult;
import cn.maiaimei.framework.exception.BusinessException;
import cn.maiaimei.framework.mybatisplus.util.PageUtils;
import cn.maiaimei.framework.util.BeanUtils;
import cn.maiaimei.framework.util.JSON;
import cn.maiaimei.framework.util.SFID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    public UserResponse insert(UserRequest userRequest) {
        User user = BeanUtils.copyProperties(userRequest, User.class);
        user.setId(SFID.nextId());
        boolean flag = save(user);
        if (flag) {
            if (log.isDebugEnabled()) {
                log.debug("insertUserResult is {}", JSON.stringify(user));
            }
            return BeanUtils.copyProperties(user, UserResponse.class);
        }
        throw new BusinessException("创建用户失败");
    }

    public UserResponse update(UserRequest userRequest) {
        User user = BeanUtils.copyProperties(userRequest, User.class);
        boolean flag = updateById(user);
        if (flag) {
            if (log.isDebugEnabled()) {
                log.debug("updateUserResult is {}", JSON.stringify(user));
            }
            return BeanUtils.copyProperties(user, UserResponse.class);
        }
        throw new BusinessException("修改用户失败");
    }

    public String delete(Long id) {
        boolean flag = removeById(id);
        return flag ? "删除用户成功" : "删除用户失败";
    }

    public UserResponse get(Long id) {
        User user = getById(id);
        if (null != user) {
            if (log.isDebugEnabled()) {
                log.debug("getUserResult is {}", JSON.stringify(user));
            }
            return BeanUtils.copyProperties(user, UserResponse.class);
        }
        throw new BusinessException("获取用户失败");
    }

    public PagingResult<UserResponse> pageQuery(Integer current, Integer size, UserQueryRequest userQueryRequest) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(userQueryRequest.getNickname()), User::getNickname, userQueryRequest.getNickname());
        wrapper.like(StringUtils.isNotBlank(userQueryRequest.getUsername()), User::getUsername, userQueryRequest.getUsername());
        Page<User> page = page(new Page<>(current, size), wrapper);
        if (log.isDebugEnabled()) {
            log.debug("pageQueryUserResult is {}", JSON.stringify(page));
        }
        return PageUtils.generate(page, UserResponse.class);
    }
}
