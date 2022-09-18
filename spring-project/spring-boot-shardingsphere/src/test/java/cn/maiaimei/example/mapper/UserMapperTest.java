package cn.maiaimei.example.mapper;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.maiaimei.example.model.User;
import cn.maiaimei.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserService userService;
    
    @Test
    public void testInsert()
    {
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        long id = snowflake.nextId();
        log.info("id={}",id);
        User user = User.builder()
                .id(id)
                .nickname("张三")
                .username("zhangs")
                .password("12345")
                .build();
        userMapper.insert(user);
    }

    @Test
    public void testUpdate()
    {
        User user = User.builder()
                .id(1570441809568600064L)
                .nickname("张三")
                .username("zhangs")
                .password("zhangs@123456")
                .build();
        userMapper.updateById(user);
    }

    @Test
    public void testDelete()
    {
        userMapper.deleteById(1570441809568600064L);
    }

    @Test
    public void testSelect()
    {
        log.info("{}",userService.list());
        log.info("{}",userService.list());
        log.info("{}",userService.list());
        log.info("{}",userService.list());
    }
}
