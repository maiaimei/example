package cn.maiaimei.example.sigleton;

public class SingletonA {
    private static SingletonA instance;

    public synchronized static SingletonA getInstance() {
        if (instance == null) {
            instance = new SingletonA();
        }
        return instance;
    }

    private SingletonA() {
        throw new UnsupportedOperationException();
    }
}
