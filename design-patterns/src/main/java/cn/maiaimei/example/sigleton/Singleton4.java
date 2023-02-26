package cn.maiaimei.example.sigleton;

public class Singleton4 {
    private static final Object LOCK_OBJECT = new Object();
    private static Singleton4 instance;

    public static Singleton4 getInstance() {
        if (instance == null) {
            // 实例锁/对象锁
            synchronized (LOCK_OBJECT) {
                if (instance == null) {
                    instance = new Singleton4();
                }
            }
        }
        return instance;
    }

    private Singleton4() {

    }
}
