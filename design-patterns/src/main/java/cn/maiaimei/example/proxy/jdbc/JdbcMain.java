package cn.maiaimei.example.proxy.jdbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Slf4j
public class JdbcMain {
    @SneakyThrows
    public static void main(String[] args) {
        String sql = "select 1 from dual";
        Connection connection = DBCP.getConnection();
        log.info("poolSize: {}", DBCP.getPoolSize());
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        boolean result = preparedStatement.execute();
        preparedStatement.close();
        connection.close();
        log.info("result: {}, poolSize: {}", result, DBCP.getPoolSize());
    }
}
