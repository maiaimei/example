package cn.maiaimei.example.service;

import cn.maiaimei.example.pojo.model.UserQueryRequest;
import cn.maiaimei.example.pojo.model.UserRequest;
import cn.maiaimei.example.pojo.model.UserResponse;
import cn.maiaimei.framework.beans.PagingResult;

public interface UserService {
    UserResponse insert(UserRequest userRequest);

    UserResponse update(UserRequest userRequest);

    int delete(Long id);

    UserResponse get(Long id);

    PagingResult<UserResponse> pageQuery(UserQueryRequest userQueryRequest, Integer current, Integer size);
}
