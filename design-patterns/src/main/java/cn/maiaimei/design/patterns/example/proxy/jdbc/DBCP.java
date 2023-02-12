package cn.maiaimei.design.patterns.example.proxy.jdbc;

import lombok.SneakyThrows;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Database Connection Pooling
 * https://blog.csdn.net/qq_55680321/article/details/122333360
 */
public class DBCP {
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://192.168.1.12:3306/testdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final Integer INITIAL_POOL_SIZE = 10;
    private static final LinkedList<Connection> POOL = new LinkedList<>();

    static {
        for (Integer i = 0; i < INITIAL_POOL_SIZE; i++) {
            Connection connection = null;
            try {
                Class.forName(DRIVER_NAME);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            // 使用静态代理创建数据库连接对象
            // ConnectionStaticProxy connectionProxy = new ConnectionStaticProxy(connection);
            // 使用JDK动态代理创建数据库连接对象
            Connection connectionProxy = (Connection) Proxy.newProxyInstance(
                    ClassLoader.getSystemClassLoader(),
                    new Class[]{Connection.class},
                    new ConnectionJDKDynamicProxy(connection)
            );
            POOL.add(connectionProxy);
        }
    }

    private DBCP() {
        throw new UnsupportedOperationException();
    }

    @SneakyThrows
    public static Connection getConnection() {
        if (POOL.size() == 0) {
            throw new Exception("no connection available");
        }
        return POOL.remove();
    }

    public static void releaseConnection(Connection connection) {
        POOL.add(connection);
    }

    public static int getPoolSize() {
        return POOL.size();
    }
}
