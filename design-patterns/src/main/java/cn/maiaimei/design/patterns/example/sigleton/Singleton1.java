package cn.maiaimei.design.patterns.example.sigleton;

public class Singleton1 {
    private static final Singleton1 INSTANCE = new Singleton1();

    public synchronized static Singleton1 getInstance() {
        return INSTANCE;
    }

    private Singleton1() {
        
    }
}
