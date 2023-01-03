package cn.maiaimei.example.strategy;

public class StrategyB implements Strategy {
    @Override
    public void doSomething() {
        System.out.println("StrategyB.doSomething");
    }
}
