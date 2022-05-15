package cn.maiaimei.example.service;

import cn.maiaimei.example.entity.User;
import cn.maiaimei.example.mapper.UserMapper;
import cn.maiaimei.example.model.UserRequest;
import cn.maiaimei.example.model.UserResponse;
import cn.maiaimei.framework.beans.PageResult;
import cn.maiaimei.framework.exception.BusinessException;
import cn.maiaimei.framework.mybatis.plus.util.PageUtils;
import cn.maiaimei.framework.spring.boot.beans.SFID;
import cn.maiaimei.framework.util.BeanUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    public UserResponse create(UserRequest userRequest) {
        User user = BeanUtils.copyProperties(userRequest, User.class);
        user.setId(SFID.nextId());
        boolean flag = save(user);
        if (flag) {
            return BeanUtils.copyProperties(user, UserResponse.class);
        }
        throw new BusinessException("创建用户失败");
    }

    public UserResponse update(UserRequest userRequest) {
        User user = BeanUtils.copyProperties(userRequest, User.class);
        boolean flag = updateById(user);
        if (flag) {
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
            return BeanUtils.copyProperties(user, UserResponse.class);
        }
        throw new BusinessException("获取用户失败");
    }

    public List<UserResponse> listAll() {
        List<User> users = list();
        return BeanUtils.copyList(users, UserResponse.class);
    }

    public PageResult pageQuery(Integer current,
                                Integer size) {
        Page page = page(new Page(current, size));
        return PageUtils.getPageResult(page, UserResponse.class);
    }
}
