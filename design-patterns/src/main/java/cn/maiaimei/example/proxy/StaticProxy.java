package cn.maiaimei.example.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * 静态代理
 */
@Slf4j
public class StaticProxy {
    public static void main(String[] args) {
        RealSubject realSubject = new RealSubject();
        ProxySubject proxySubject = new ProxySubject(realSubject);
        proxySubject.request();
    }

    interface Subject {
        void request();
    }

    static class RealSubject implements Subject {
        @Override
        public void request() {
            log.info("request");
        }
    }

    static class ProxySubject implements Subject {
        private RealSubject target;

        public ProxySubject(RealSubject target) {
            this.target = target;
        }

        @Override
        public void request() {
            log.info("before");
            target.request();
            log.info("after");
        }
    }
}
