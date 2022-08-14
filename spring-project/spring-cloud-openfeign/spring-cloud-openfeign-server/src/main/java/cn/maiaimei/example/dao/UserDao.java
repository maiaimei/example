package cn.maiaimei.example.dao;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.maiaimei.example.model.UserModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class UserDao {
    private static final String INSERT_SQL = "insert into sys_user (id,nickname,username,password) values (?,?,?,?)";
    private static final String UPDATE_SQL = "update sys_user set nickname=?,username=?,password=? where id=?";
    private static final String DELETE_SQL = "delete from sys_user where id=?";
    private static final String SELECT_SQL = "select * from sys_user limit ?,?";
    private static final String SELECT_ONE_SQL = "select * from sys_user where id=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static Snowflake snowflake;

    static {
        snowflake = IdUtil.getSnowflake(1L, 1L);
    }

    public int create(UserModel userModel) {
        userModel.setId(snowflake.nextId());
        return jdbcTemplate.update(INSERT_SQL, userModel.getId(), userModel.getNickname(), userModel.getUsername(), userModel.getPassword());
    }

    public int update(UserModel userModel) {
        return jdbcTemplate.update(UPDATE_SQL, userModel.getNickname(), userModel.getUsername(), userModel.getPassword(), userModel.getId());
    }

    public int delete(Long id) {
        return jdbcTemplate.update(DELETE_SQL, id);
    }

    public UserModel selectOne(Long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_ONE_SQL, new BeanPropertyRowMapper<>(UserModel.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @SneakyThrows
    public List<UserModel> selectList(Integer current, Integer size) {
        int begin = (current - 1) * size;
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(SELECT_SQL, begin, size);
        ObjectMapper mapper = new ObjectMapper();
        String mapsAsString = mapper.writeValueAsString(maps);
        return mapper.readValue(mapsAsString, new TypeReference<List<UserModel>>() {
        });
    }
}
