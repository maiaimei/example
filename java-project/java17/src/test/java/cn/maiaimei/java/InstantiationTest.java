package cn.maiaimei.java;

/**
 * 静态代码块、普通代码块、构造方法的执行顺序
 * 父类 > 子类
 * 静态代码块 > 普通代码块 > 构造方法
 * ----------------------------------------
 * 父类静态代码块
 * 子类静态代码块
 * 父类普通代码块
 * 父类构造方法
 * 子类普通代码块
 * 子类构造方法
 */
public class InstantiationTest {

    public static void main(String[] args) {
        new InstantiationA();
    }
    
    public static class InstantiationA extends InstantiationB {
        static {
            System.out.println("子类静态代码块");
        }

        {
            System.out.println("子类普通代码块");
        }

        public InstantiationA(){
            System.out.println("子类构造方法");
        }
    }

    public static class InstantiationB {
        static {
            System.out.println("父类静态代码块");
        }

        {
            System.out.println("父类普通代码块");
        }

        public InstantiationB(){
            System.out.println("父类构造方法");
        }
    }
}
