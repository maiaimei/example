package cn.maiaimei.java;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金额需要用BigDecimal，不可用float、double
 */
public class BigDecimalTest {
    public static void main(String[] args) {
        double a = 1;
        double b = 0.9;
        System.out.println(a-b); // 0.09999999999999998

        BigDecimal d1 = BigDecimal.valueOf(1);
        BigDecimal d2 = BigDecimal.valueOf(0.9);
        BigDecimal d3 = d1.subtract(d2);
        System.out.println(d3); // 0.1

        BigDecimal d4 = BigDecimal.valueOf(10);
        BigDecimal d5 = BigDecimal.valueOf(3);
        // BigDecimal d6 = d4.divide(d5); // java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result.
        BigDecimal d6 = d4.divide(d5,2, RoundingMode.CEILING);
        System.out.println(d6);
    }
}
