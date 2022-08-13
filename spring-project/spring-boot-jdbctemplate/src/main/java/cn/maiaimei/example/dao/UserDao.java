package cn.maiaimei.example.dao;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.maiaimei.example.config.SqlConfig;
import cn.maiaimei.example.model.UserModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class UserDao {
    private static final String INSERT_SQL = "insert into sys_user (id,nickname,username,password) values (?,?,?,?)";
    private static final String UPDATE_SQL = "update sys_user set nickname=?,username=?,password=? where id=?";
    private static final String DELETE_SQL = "delete from sys_user where id=?";
    private static final String SELECT_SQL = "select * from sys_user";
    private static final String SELECT_ONE_SQL = "select * from sys_user where id=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static Snowflake snowflake;

    static {
        snowflake = IdUtil.getSnowflake(1L, 1L);
    }

    public int create(UserModel userModel) {
        //String sql = INSERT_SQL;
        String sql = getSql("user.insert");
        userModel.setId(snowflake.nextId());
        return jdbcTemplate.update(sql, userModel.getId(), userModel.getNickname(), userModel.getUsername(), userModel.getPassword());
    }

    public int update(UserModel userModel) {
        String sql = getSql("user.update");
        return jdbcTemplate.update(sql, userModel.getNickname(), userModel.getUsername(), userModel.getPassword(), userModel.getId());
    }

    public int delete(Long id) {
        return jdbcTemplate.update(DELETE_SQL, id);
    }

    public List<UserModel> batchCreate(List<UserModel> userModels) {
        List<Object[]> batchArgs = new ArrayList<>();
        for (UserModel userModel : userModels) {
            userModel.setId(snowflake.nextId());
            batchArgs.add(new Object[]{snowflake.nextId(), userModel.getNickname(), userModel.getUsername(), userModel.getPassword()});
        }
        jdbcTemplate.batchUpdate(INSERT_SQL, batchArgs);
        return userModels;
    }

    public UserModel selectOne(Long id) {
        return jdbcTemplate.queryForObject(SELECT_ONE_SQL, new BeanPropertyRowMapper<>(UserModel.class), id);
    }

    public List<UserModel> selectList() {
        List<UserModel> users = new ArrayList<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(SELECT_SQL);
        for (Map<String, Object> map : maps) {
            UserModel user = new UserModel();
            user.setId(Long.parseLong(map.get("id").toString()));
            user.setNickname(map.get("nickname").toString());
            user.setUsername(map.get("username").toString());
            user.setPassword(map.get("password").toString());
            users.add(user);
        }
        return users;
    }

    @SneakyThrows
    public List<UserModel> queryList() {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(SELECT_SQL);
        ObjectMapper mapper = new ObjectMapper();
        String mapsAsString = mapper.writeValueAsString(maps);
        return mapper.readValue(mapsAsString, new TypeReference<List<UserModel>>() {
        });
    }

    private String getSql(String key) {
        return SqlConfig.USER_SQLS.get(key);
    }
}
