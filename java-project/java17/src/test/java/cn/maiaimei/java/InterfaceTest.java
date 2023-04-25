package cn.maiaimei.java;

public class InterfaceTest {
    
    /**
     * 接口不能继承类，但可以继承多个接口
     * Java 8 支持默认方法、静态方法
     * Java 9 支持私有方法
     */
    public interface InterfaceA extends InterfaceB, InterfaceC {
        void m1();

        /**
         * Java 8 support
         */
        default void m2(){}

        /**
         * Java 8 support
         */
        static void m6(){}

        /**
         * Java 9 support
         */
        private void m3(){}
    }

    public interface InterfaceB {
        void m4();
    }

    public interface InterfaceC {
        void m5();
    }
}
