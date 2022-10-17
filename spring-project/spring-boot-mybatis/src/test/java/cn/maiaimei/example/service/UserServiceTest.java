package cn.maiaimei.example.service;

import cn.maiaimei.example.TestBase;
import cn.maiaimei.example.mapper.UserMapper;
import cn.maiaimei.example.pojo.entity.User;
import cn.maiaimei.example.pojo.model.UserQueryRequest;
import cn.maiaimei.example.pojo.model.UserRequest;
import cn.maiaimei.example.pojo.model.UserResponse;
import cn.maiaimei.framework.beans.PagingResult;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest extends TestBase {
    @Autowired
    UserService userService;

    @MockBean
    UserMapper userMapper;

    @Test
    void testInsert() {
        UserRequest request = readFileAsObject(INSERT_REQUEST, UserRequest.class);
        when(userMapper.insert(any())).thenReturn(1);
        UserResponse actualResponse = userService.insert(request);
        assertEquals(request.getUsername(), actualResponse.getUsername());
    }

    @Test
    void testUpdate() {
        UserRequest request = readFileAsObject(USER, UserRequest.class);
        when(userMapper.update(any())).thenReturn(1);
        UserResponse actualResponse = userService.update(request);
        assertEquals(request.getId(), actualResponse.getId());
    }

    @Test
    void testDelete() {
        when(userMapper.delete(anyLong())).thenReturn(1);
        int actualResponse = userService.delete(1581965723515883520L);
        assertEquals(1, actualResponse);
    }

    @Test
    void testGet() {
        User user = readFileAsObject(USER, User.class);
        when(userMapper.get(anyLong())).thenReturn(user);
        UserResponse actualResponse = userService.get(1581965723515883520L);
        assertEquals(user.getId(), actualResponse.getId());
    }

    @Test
    void testPageQuery() {
        UserQueryRequest request = readFileAsObject(PAGE_QUERY_REQUEST, UserQueryRequest.class);
        PagingResult<UserResponse> response = readFileAsObject(PAGE_QUERY_RESPONSE, new TypeReference<PagingResult<UserResponse>>() {
        });
        List<User> result = readFileAsObject(PAGE_QUERY_RESULT, new TypeReference<List<User>>() {
        });
        when(userMapper.pageQuery(any(), anyInt(), anyInt())).thenReturn(result);
        when(userMapper.pageQueryCount(any())).thenReturn(9);
        PagingResult<UserResponse> actualResponse = userService.pageQuery(request, 1, 3);
        assertEquals(response.getRecords().size(), actualResponse.getRecords().size());
    }
}
