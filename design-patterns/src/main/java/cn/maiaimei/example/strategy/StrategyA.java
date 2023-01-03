package cn.maiaimei.example.strategy;

public class StrategyA implements Strategy {
    @Override
    public void doSomething() {
        System.out.println("StrategyA.doSomething");
    }
}
