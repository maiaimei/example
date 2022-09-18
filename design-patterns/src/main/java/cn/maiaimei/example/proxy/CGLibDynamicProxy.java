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
                log.info("before");
                Object result = proxy.invokeSuper(target, args);
                log.info("after");
                return result;
            }
        });
        RealSubject proxySubject = (RealSubject) enhancer.create();
        proxySubject.request();
    }

    static class RealSubject {
        public void request() {
            log.info("request");
        }
    }
}
