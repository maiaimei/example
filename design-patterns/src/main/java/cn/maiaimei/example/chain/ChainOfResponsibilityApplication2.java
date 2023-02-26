package cn.maiaimei.example.chain;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChainOfResponsibilityApplication2 {

	public static void main(String[] args) {
		ConcreteHandlerA handlerA = new ConcreteHandlerA();
		List<Integer> numbers = Arrays.asList(1, 4, 6, 2, 10, 23, 31, 60, 180);
        for (Integer number : numbers) {
        	handlerA.handleRequest(number);
        }
	}
	
	/**
     * 抽象处理类
     */
    static abstract class Handler {
        public abstract void handleRequest(int number);
        
        public abstract Handler getNextHandler();
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
        
        @Override
        public Handler getNextHandler() {
        	return new ConcreteHandlerB();
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
        
        @Override
        public Handler getNextHandler() {
        	return new ConcreteHandlerC();
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
        
        @Override
        public Handler getNextHandler() {
        	return new ConcreteHandlerD();
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
        
        @Override
        public Handler getNextHandler() {
        	return null;
        }
    }


}
