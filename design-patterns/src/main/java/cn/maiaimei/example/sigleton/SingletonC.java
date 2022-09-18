package cn.maiaimei.example.sigleton;

public class SingletonC {
    private static Object obj = new Object();
    private static SingletonC instance;

    public static SingletonC getInstance() {
        if (instance == null) {
            // 实例锁/对象锁
            synchronized (obj) {
                if (instance == null) {
                    instance = new SingletonC();
                }
            }
        }
        return instance;
    }

    private SingletonC() {
        throw new UnsupportedOperationException();
    }
}
