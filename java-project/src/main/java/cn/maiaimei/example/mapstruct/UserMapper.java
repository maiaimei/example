package cn.maiaimei.example.mapstruct;

import cn.maiaimei.example.pojo.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto copyUserDto(UserDto userDto);
}
