package cn.maiaimei.example.sigleton;

public class SingletonB {
    private static SingletonB instance;

    public static SingletonB getInstance() {
        if (instance == null) {
            // 全局锁/类锁
            //synchronized (SingletonB.class) {
            synchronized (SingletonB.class.getClassLoader()) {
                if (instance == null) {
                    instance = new SingletonB();
                }
            }
        }
        return instance;
    }

    private SingletonB() {
        throw new UnsupportedOperationException();
    }
}
