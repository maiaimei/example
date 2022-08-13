package cn.maiaimei.example.dao;

import cn.maiaimei.example.model.UserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {
    @InjectMocks
    UserDao userDao;

    @Mock
    JdbcTemplate jdbcTemplate;

    @Test
    void testCreate() {
        when(jdbcTemplate.update(any(), any(), any(), any(), any())).thenReturn(1);
        int actual = userDao.create(new UserModel());
        assertEquals(1, actual);
    }

    @Test
    void testUpdate() {
        when(jdbcTemplate.update(any(), any(), any(), any(), any())).thenReturn(1);
        int actual = userDao.update(new UserModel());
        assertEquals(1, actual);
    }

    @Test
    void testDelete() {
        when(jdbcTemplate.update(any(), anyLong())).thenReturn(1);
        int actual = userDao.delete(1L);
        assertEquals(1, actual);
    }

    @Test
    void testBatchCreate() {
        List<UserModel> users = new ArrayList<>();
        users.add(new UserModel());
        when(jdbcTemplate.batchUpdate(any(), anyList())).thenReturn(new int[0]);
        List<UserModel> actual = userDao.batchCreate(users);
        assertNotNull(actual);
    }

    @Test
    void testSelectOne() {
        UserModel user = new UserModel();
        when(jdbcTemplate.queryForObject(any(), any(BeanPropertyRowMapper.class), anyLong())).thenReturn(user);
        UserModel actual = userDao.selectOne(1L);
        assertEquals(user, actual);
    }

    @Test
    void testSelectList() {
        List<Map<String, Object>> maps = new ArrayList<>();
        maps.add(new HashMap<String, Object>() {{
            put("id", "1558300618140028928");
            put("nickname", "test");
            put("username", "test");
            put("password", "test");
        }});
        when(jdbcTemplate.queryForList(any())).thenReturn(maps);
        List<UserModel> actual = userDao.selectList();
        assertNotNull(actual);
    }

    @Test
    void testQueryList() {
        List<Map<String, Object>> maps = new ArrayList<>();
        when(jdbcTemplate.queryForList(any())).thenReturn(maps);
        List<UserModel> actual = userDao.queryList();
        assertNotNull(actual);
    }
}
