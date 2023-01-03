package cn.maiaimei.example.strategy;

public class Client {
    public static void main(String[] args) {
        Context context = new Context(new StrategyB());
        context.doWhat();
    }
}
