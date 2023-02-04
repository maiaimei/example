package cn.maiaimei.example.mapper;

import cn.maiaimei.example.pojo.entity.User;
import cn.maiaimei.example.pojo.model.UserQueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.mapper.ClassPathMapperScanner;

import java.util.List;

/**
 * {@link MybatisProperties#resolveMapperLocations()}
 * {@link ClassPathMapperScanner#doScan(java.lang.String...)}
 */
@Mapper
public interface UserMapper {
    int insert(User user);

    int update(User user);

    int delete(Long id);

    User get(Long id);

    List<User> list(UserQueryRequest user);
}
