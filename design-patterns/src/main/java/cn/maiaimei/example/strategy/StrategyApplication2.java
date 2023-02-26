package cn.maiaimei.example.strategy;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
public class StrategyApplication2 {

    public static void main(String[] args) {
        Context context = new Context();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int n = random.nextInt(10);
            context.handleRequest(n);
        }
    }

    interface Strategy {
        boolean support(int n);

        void handleRequest();
    }

    static class ConcreteStrategy1 implements Strategy {
        @Override
        public boolean support(int n) {
            return n > 5;
        }

        @Override
        public void handleRequest() {
            log.info("{} 处理请求", this.getClass().getSimpleName());
        }
    }

    static class ConcreteStrategy2 implements Strategy {
        @Override
        public boolean support(int n) {
            return n < 5;
        }

        @Override
        public void handleRequest() {
            log.info("{} 处理请求", this.getClass().getSimpleName());
        }
    }

    static class Context {
        private final ConcreteStrategy1 concreteStrategy1 = new ConcreteStrategy1();
        private final ConcreteStrategy2 concreteStrategy2 = new ConcreteStrategy2();
        private final List<Strategy> strategies = Arrays.asList(concreteStrategy1, concreteStrategy2);

        public void handleRequest(int n) {
            for (Strategy strategy : strategies) {
                if (strategy.support(n)) {
                    strategy.handleRequest();
                }
            }
        }
    }

}
