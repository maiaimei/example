package cn.maiaimei.example.mapper;

import cn.maiaimei.example.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("insert into sys_user(id, username, password, nickname) values (#{user.id}, #{user.username}, #{user.password}, #{user.nickname})")
    int create(@Param("user") User user);

    @Update("update sys_user set username=#{user.username}, password=#{user.password}, nickname=#{user.nickname} where id=#{user.id}")
    int update(@Param("user") User user);

    @Delete("delete sys_user where id=#{id}")
    int delete(@Param("id") Long id);

    //@Select()
    User get(@Param("id") Long id);

    List<User> listAll();

    List<User> pageQuery(@Param("from") Integer from,
                         @Param("size") Integer size);
}