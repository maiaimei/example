package cn.maiaimei.example.mapper;

import cn.maiaimei.example.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    List<String> getPermission(@Param("id") Long id);
}