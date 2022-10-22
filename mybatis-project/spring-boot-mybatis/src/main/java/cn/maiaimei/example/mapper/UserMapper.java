package cn.maiaimei.example.mapper;

import cn.maiaimei.example.pojo.entity.User;
import cn.maiaimei.example.pojo.model.UserQueryRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    int insert(User user);

    int update(User user);

    int delete(Long id);

    User get(Long id);

    List<User> list(UserQueryRequest user);
}
