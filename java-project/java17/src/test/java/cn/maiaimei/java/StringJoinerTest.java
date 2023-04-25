package cn.maiaimei.java;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class StringJoinerTest {
    public static void main(String[] args) {
        String[] strings = new String[]{"A","B","C"};
        
        // String.join
        System.out.println(String.join(",", strings));
        
        // StringJoiner
        final StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (String s : strings) {
            joiner.add(s);
        }
        System.out.println(joiner);

        // Stream API, Collectors.joining, 底层使用了StringJoiner
        System.out.println(Arrays.stream(strings).collect(Collectors.joining(",", "[", "]")));
    }
}
