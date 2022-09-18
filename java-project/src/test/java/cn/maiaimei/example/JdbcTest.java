package cn.maiaimei.example;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.maiaimei.example.model.UserModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JdbcTest {
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://192.168.1.12:3306/testdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    private static Snowflake snowflake;

    static {
        snowflake = IdUtil.getSnowflake(1L, 1L);
    }

    @SneakyThrows
    @Test
    public void testQueryList() {
        String sql = "select id,nickname,username,password from sys_user";
        List<UserModel> users = new ArrayList<>();
        UserModel user;
        Class.forName(DRIVER_NAME);
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            user = new UserModel();
            user.setId(resultSet.getLong("id"));
            user.setNickname(resultSet.getString("nickname"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            users.add(user);
        }
        resultSet.close();
        statement.close();
        connection.close();
        log.info("users: {}", users);
    }

    @SneakyThrows
    @Test
    public void testQueryOne() {
        UserModel user = new UserModel();
        String sql = "select id,nickname,username,password from sys_user where id = ?";
        Class.forName(DRIVER_NAME);
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, 1562417697810157569L);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            user.setId(resultSet.getLong(1));
            user.setNickname(resultSet.getString(2));
            user.setNickname(resultSet.getString(3));
            user.setNickname(resultSet.getString(4));
        }
        resultSet.close();
        statement.close();
        connection.close();
        log.info("user: {}", user);
    }

    @SneakyThrows
    @Test
    public void testInsert() {
        String sql = "insert into sys_user (id,nickname,username,password) values (?,?,?,?)";
        Class.forName(DRIVER_NAME);
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, snowflake.nextId());
        statement.setString(2, "test");
        statement.setString(3, "test");
        statement.setString(4, "test");
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    @SneakyThrows
    @Test
    public void testUpdate() {
        String sql = "update sys_user set nickname=?,username=?,password=? where id=?";
        Class.forName(DRIVER_NAME);
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, "tester");
        statement.setString(2, "tester");
        statement.setString(3, "tester");
        statement.setLong(4, 1562430619957465088L);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    @SneakyThrows
    @Test
    public void testDelete() {
        String sql = "delete from sys_user where id=?";
        Class.forName(DRIVER_NAME);
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, 1562430619957465088L);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }
}

