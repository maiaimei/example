package cn.maiaimei.design.patterns.example.proxy.jdbc;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;

@Slf4j
public class ConnectionJDKDynamicProxy implements InvocationHandler {
    private Connection connection;

    public ConnectionJDKDynamicProxy(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("close")) {
            log.info("close");
            DBCP.releaseConnection(this.connection);
            return null;
        }
        return method.invoke(this.connection, args);
    }
}
