package cn.maiaimei.design.patterns.example.chain;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * 责任链模式(Chain of Responsibility)，行为型设计模式，
 * 是一种处理请求的模式，多个对象通过前一对象记住其下一对象的引用而串成链，
 * 每个对象都有机会处理客户端发出的请求，直到链上某个对象处理请求成功为止。
 * 应用场景：审批工作流
 */
@Slf4j
public class ChainOfResponsibilityApplication {

    public static void main(String[] args) {
        ConcreteHandler1 handler1 = new ConcreteHandler1();
        ConcreteHandler2 handler2 = new ConcreteHandler2();
        ConcreteHandler3 handler3 = new ConcreteHandler3();
        ConcreteHandler4 handler4 = new ConcreteHandler4();
        //handler1.setNextHandler(handler2);
        //handler2.setNextHandler(handler3);
        //handler3.setNextHandler(handler4);
        handler1.appendNextHandler(handler2)
                .appendNextHandler(handler3)
                .appendNextHandler(handler4);

        List<Integer> numbers = Arrays.asList(1, 4, 6, 2, 10, 23, 31, 60, 180);
        for (Integer number : numbers) {
            handler1.handleRequest(number);
        }
    }

    /**
     * 抽象处理类
     */
    static abstract class Handler {
        private Handler nextHandler;

        protected Handler getNextHandler() {
            return nextHandler;
        }

        protected void setNextHandler(Handler nextHandler) {
            this.nextHandler = nextHandler;
        }

        protected Handler appendNextHandler(Handler nextHandler) {
            this.nextHandler = nextHandler;
            return nextHandler;
        }

        public abstract void handleRequest(int number);
    }

    /**
     * 具体处理类 1
     */
    static class ConcreteHandler1 extends Handler {

        @Override
        public void handleRequest(int number) {
            if (number > 0 && number <= 3) {
                log.info("{} 处理请求 {}", this.getClass().getSimpleName(), number);
            } else if (getNextHandler() != null) {
                getNextHandler().handleRequest(number);
            }
        }
    }

    /**
     * 具体处理类 2
     */
    static class ConcreteHandler2 extends Handler {

        @Override
        public void handleRequest(int number) {
            if (number > 3 && number <= 10) {
                log.info("{} 处理请求 {}", this.getClass().getSimpleName(), number);
            } else if (getNextHandler() != null) {
                getNextHandler().handleRequest(number);
            }
        }
    }

    /**
     * 具体处理类 3
     */
    static class ConcreteHandler3 extends Handler {

        @Override
        public void handleRequest(int number) {
            if (number > 10 && number <= 30) {
                log.info("{} 处理请求 {}", this.getClass().getSimpleName(), number);
            } else if (getNextHandler() != null) {
                getNextHandler().handleRequest(number);
            }
        }
    }

    /**
     * 具体处理类 4
     */
    static class ConcreteHandler4 extends Handler {

        @Override
        public void handleRequest(int number) {
            if (number > 30) {
                log.info("{} 处理请求 {}", this.getClass().getSimpleName(), number);
            } else if (getNextHandler() != null) {
                getNextHandler().handleRequest(number);
            }
        }
    }

}
