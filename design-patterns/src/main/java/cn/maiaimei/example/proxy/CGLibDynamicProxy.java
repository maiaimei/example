package cn.maiaimei.example.proxy;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLib动态代理
 */
@Slf4j
public class CGLibDynamicProxy {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealSubject.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                if ("handleRequest".equals(method.getName())) {
                    log.info("{} 前置处理请求", proxy.getClass().getSimpleName());
                    Object result = proxy.invokeSuper(target, args);
                    log.info("{} 后置处理请求", proxy.getClass().getSimpleName());
                    return result;
                }
                return null;
            }
        });
        RealSubject proxySubject = (RealSubject) enhancer.create();
        proxySubject.handleRequest();
    }

    static class RealSubject {
        public void handleRequest() {
            log.info("{} 处理请求", this.getClass().getSimpleName());
        }
    }
}
