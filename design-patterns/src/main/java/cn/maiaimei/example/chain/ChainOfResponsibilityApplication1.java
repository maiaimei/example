package cn.maiaimei.example.chain;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * 职责链、责任链模式(Chain of Responsibility)，行为型设计模式，
 * 是一种处理请求的模式，多个对象通过前一对象记住其下一对象的引用而串成链，
 * 每个对象都有机会处理客户端发出的请求，直到链上某个对象处理请求成功为止。
 * 应用场景：审批工作流
 */
@Slf4j
public class ChainOfResponsibilityApplication1 {

    public static void main(String[] args) {
        ConcreteHandlerA handlerA = new ConcreteHandlerA();
        ConcreteHandlerB handlerB = new ConcreteHandlerB();
        ConcreteHandlerC handlerC = new ConcreteHandlerC();
        ConcreteHandlerD handlerD = new ConcreteHandlerD();
        //handler1.setNextHandler(handlerB);
        //handler2.setNextHandler(handlerC);
        //handler3.setNextHandler(handlerD);
        handlerA.appendNextHandler(handlerB)
                .appendNextHandler(handlerC)
                .appendNextHandler(handlerD);

        List<Integer> numbers = Arrays.asList(1, 4, 6, 2, 10, 23, 31, 60, 180);
        for (Integer number : numbers) {
        	handlerA.handleRequest(number);
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
    static class ConcreteHandlerA extends Handler {

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
    static class ConcreteHandlerB extends Handler {

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
    static class ConcreteHandlerC extends Handler {

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
    static class ConcreteHandlerD extends Handler {

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
