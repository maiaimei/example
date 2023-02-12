package cn.maiaimei.design.patterns.example.strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StrategyApplication {

    public static void main(String[] args) {
        Context context = new Context(new ConcreteStrategy2());
        context.handleRequest();
    }

    interface Strategy {
        void handleRequest();
    }

    static class ConcreteStrategy1 implements Strategy {
        @Override
        public void handleRequest() {
            log.info("{} 处理请求", this.getClass().getSimpleName());
        }
    }

    static class ConcreteStrategy2 implements Strategy {
        @Override
        public void handleRequest() {
            log.info("{} 处理请求", this.getClass().getSimpleName());
        }
    }

    static class Context {
        private final Strategy strategy;

        public Context(Strategy strategy) {
            this.strategy = strategy;
        }

        public void handleRequest() {
            this.strategy.handleRequest();
        }
    }
}
