package cn.maiaimei.example.service;

import cn.maiaimei.example.TestBase;
import cn.maiaimei.example.mapper.UserMapper;
import cn.maiaimei.example.pojo.entity.User;
import cn.maiaimei.example.pojo.model.UserQueryRequest;
import cn.maiaimei.example.pojo.model.UserRequest;
import cn.maiaimei.example.pojo.model.UserResponse;
import cn.maiaimei.framework.beans.PagingResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest extends TestBase {
    @Autowired
    UserService userService;

    @MockBean
    UserMapper userMapper;

    @Test
    void testInsert() {
        UserRequest request = readFileAsObject(INSERT_USER_REQUEST, UserRequest.class);
        when(userMapper.insert(any())).thenReturn(1);
        UserResponse actual = userService.insert(request);
        assertEquals(request.getUsername(), actual.getUsername());
    }

    @Test
    void testUpdate() {
        UserRequest request = readFileAsObject(UPDATE_USER_REQUEST, UserRequest.class);
        when(userMapper.updateById(any())).thenReturn(1);
        UserResponse actual = userService.update(request);
        assertEquals(request.getId(), actual.getId());
    }

    //@Test
    void testDelete() {
        when(userMapper.deleteById(anyLong())).thenReturn(1);
        userService.delete(1582383391087661056L);
    }

    @Test
    void testGet() {
        User user = readFileAsObject(GET_USER_RESULT, User.class);
        when(userMapper.selectById(anyLong())).thenReturn(user);
        UserResponse actual = userService.get(1581924449823887360L);
        assertEquals(user.getId(), actual.getId());
    }

    @Test
    void testPageQuery() {
        UserQueryRequest request = readFileAsObject(PAGE_QUERY_USER_REQUEST, UserQueryRequest.class);
        PagingResult<UserResponse> expected = readFileAsObject(PAGE_QUERY_USER_RESPONSE, new TypeReference<PagingResult<UserResponse>>() {
        });
        Page<User> page = readFileAsObject(PAGE_QUERY_USER_RESULT, new TypeReference<Page<User>>() {
        });
        when(userMapper.selectPage(any(IPage.class), any(Wrapper.class))).thenReturn(page);
        PagingResult<UserResponse> actual = userService.pageQuery(1, 3, request);
        assertEquals(expected.getRecords().size(), actual.getRecords().size());
    }
}
