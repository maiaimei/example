package cn.maiaimei.java;

/**
 * 包装类
 * 装箱拆箱原理&包装类型缓冲池
 */
public class WrapperClassTest {
    public static void main(String[] args) {
        testIntegerCache();
        testBoxing();
    }

    /**
     * 缓冲池
     */
    private static void testIntegerCache(){
        Integer a = 1;
        Integer b = 1;
        System.out.println(a == b); // true

        // 128 不在 IntegerCache范围内，会创建新的Integer
        Integer c = 128;
        Integer d = 128;
        System.out.println(c == d); // false
        System.out.println(c.equals(d)); // true
    }

    /**
     * 装箱
     */
    private static void testBoxing(){
        // 自动装箱
        int a = 1;
        Integer b = a;
        // 手动装箱
        Integer c = Integer.valueOf(a);
    }

    /**
     * 拆箱
     */
    private static void testUnBoxing(){
        // 自动拆箱
        Integer b = 1;
        int a = b;
        // 手动拆箱
        int c = b.intValue();
    }
}
