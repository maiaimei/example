package cn.maiaimei.example.sigleton;

public class Singleton5 {

    private static final class InstanceHolder {
        static final Singleton5 INSTANCE = new Singleton5();
    }

    public static Singleton5 getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private Singleton5() {
    }
}
