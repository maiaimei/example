package cn.maiaimei.example.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理
 * {@link Proxy#newProxyInstance(java.lang.ClassLoader, java.lang.Class[], java.lang.reflect.InvocationHandler)}
 * {@link InvocationHandler}
 */
@Slf4j
public class JDKDynamicProxy {
    public static void main(String[] args) {
        RealSubject realSubject = new RealSubject();
        Subject proxySubject = (Subject) Proxy.newProxyInstance(realSubject.getClass().getClassLoader(), realSubject.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                log.info("{} 前置处理请求", proxy.getClass().getSimpleName());
                Object result = method.invoke(realSubject, args);
                log.info("{} 后置处理请求", proxy.getClass().getSimpleName());
                return result;
            }
        });
        proxySubject.handleRequest();
    }

    interface Subject {
        void handleRequest();
    }

    static class RealSubject implements Subject {
        @Override
        public void handleRequest() {
            log.info("{} 处理请求", this.getClass().getSimpleName());
        }
    }
}
