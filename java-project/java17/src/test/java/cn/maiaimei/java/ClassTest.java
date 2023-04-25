package cn.maiaimei.java;

/**
 * public class：声明在包下，任何地方都能访问
 * class：声明在包下，该类所在包能访问
 * 静态内部类：声明在类中，访问修饰符 static class
 * 成员内部类：声明在类中，访问修饰符 class
 * 局部内部类：声明在方法中
 * 匿名内部类：声明在方法中
 */
public class ClassTest {
    // 静态内部类
    private static class StaticInnerClass{
    }

    // 成员内部类
    private class MemberInnerClass{
    }

    public static void main(String[] args) {
        // 局部内部类
        class LocalInnerclass{
        }

        // 匿名内部类（可以用lambda表达式简化）
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
    }
}
