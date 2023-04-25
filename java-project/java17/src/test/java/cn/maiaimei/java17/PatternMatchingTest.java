package cn.maiaimei.java17;

/**
 * Java 17 新特性 - 模式匹配
 */
public class PatternMatchingTest {
    public static void main(String[] args) {
        patternMatchingForIf("hello");
        patternMatchingForIf(12345);
        
        patternMatchingForSwitch(1);
        patternMatchingForSwitch(1L);
        patternMatchingForSwitch(1.0);
        patternMatchingForSwitch("1");
    }
    
    private static void patternMatchingForIf(Object o){
        if(o instanceof String s){
            System.out.println("o is String, value is " + s);
        } else if(o instanceof Integer i){
            System.out.println("o is Integer, value is " + i);
        }
    }

    private static void patternMatchingForSwitch(Object o){
        switch (o) {
            case Integer i -> System.out.printf("int %d%n", i);
            case Long l    -> System.out.printf("long %d%n", l);
            case Double d  -> System.out.printf("double %f%n", d);
            case String s  -> System.out.printf("String %s%n", s);
            default        -> System.out.println(o);
        };
    }
}
