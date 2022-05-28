package cn.maiaimei.example;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class Demo01Test extends TestCase {
    private Integer m, n;

    @Override
    protected void setUp() throws Exception {
        m = 1;
        n = 1;
    }

    @Override
    protected void tearDown() throws Exception {
        m = null;
        n = null;
    }

    @Test
    public void testPlus() {
        Integer result = m + n;
        log.info("{} + {} = {}", m, n, result);
        assertEquals(2, result.intValue());
    }

    @Test
    public void testMinus() {
        Integer result = m - n;
        log.info("{} - {} = {}", m, n, result);
        assertEquals(0, result.intValue());
    }
}
