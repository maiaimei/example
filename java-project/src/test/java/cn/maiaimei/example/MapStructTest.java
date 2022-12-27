package cn.maiaimei.example;

import cn.maiaimei.example.mapstruct.UserMapper;
import cn.maiaimei.example.pojo.dto.UserDto;
import org.junit.jupiter.api.Test;

public class MapStructTest {
    @Test
    void testCopyUserDto() {
        UserDto userDto = buildUserDto();
        UserDto userDtoCopy = UserMapper.INSTANCE.copyUserDto(userDto);
        System.out.println(userDto);
        System.out.println(userDtoCopy);
    }

    UserDto buildUserDto() {
        return UserDto.builder()
                .id(1L)
                .nickname("管理员")
                .username("admin")
                .password("12345")
                .build();
    }
}
