package cn.maiaimei.example;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

/**
 * JUnit3中测试用例需要继承TestCase类
 */
@Slf4j
public class JUnit3Test extends TestCase {
    private Integer m, n;

    @Override
    protected void setUp() {
        m = 1;
        n = 1;
    }

    @Override
    protected void tearDown() {
        m = null;
        n = null;
    }

    public void testPlus() {
        Integer result = m + n;
        log.info("{} + {} = {}", m, n, result);
        assertEquals(2, result.intValue());
    }

    public void testMinus() {
        Integer result = m - n;
        log.info("{} - {} = {}", m, n, result);
        assertEquals(0, result.intValue());
    }
}
