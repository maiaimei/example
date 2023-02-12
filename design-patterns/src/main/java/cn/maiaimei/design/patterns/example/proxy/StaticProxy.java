package cn.maiaimei.design.patterns.example.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * 静态代理
 */
@Slf4j
public class StaticProxy {
    public static void main(String[] args) {
        RealSubject realSubject = new RealSubject();
        ProxySubject proxySubject = new ProxySubject(realSubject);
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

    static class ProxySubject implements Subject {
        private RealSubject target;

        public ProxySubject(RealSubject target) {
            this.target = target;
        }

        @Override
        public void handleRequest() {
            log.info("{} 前置处理请求", this.getClass().getSimpleName());
            target.handleRequest();
            log.info("{} 后置处理请求", this.getClass().getSimpleName());
        }
    }
}
