package cn.maiaimei.example.strategy;

public class Context {
    private final Strategy strategy;

    public Context(Strategy strategy) {
        this.strategy = strategy;
    }

    public void doWhat() {
        this.strategy.doSomething();
    }
}
