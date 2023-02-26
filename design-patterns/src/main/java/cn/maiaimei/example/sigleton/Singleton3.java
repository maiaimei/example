package cn.maiaimei.example.sigleton;

public class Singleton3 {
    private static Singleton3 instance;

    public static Singleton3 getInstance() {
        if (instance == null) {
            // 全局锁/类锁
            //synchronized (Singleton2.class) {
            synchronized (Singleton3.class.getClassLoader()) {
                if (instance == null) {
                    instance = new Singleton3();
                }
            }
        }
        return instance;
    }

    private Singleton3() {

    }
}
