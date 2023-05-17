package cn.maiaimei.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class Junit5Test {
    private Integer m, n;

    @BeforeAll
    public static void setUpOnce() {
        log.info("run once before any of the test methods in the class");
    }

    @BeforeEach
    public void setUp() {
        m = 1;
        n = 1;
        log.info("run before each test in the class, m={}, n={}", m, n);
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

    @AfterEach
    public void tearDown() {
        m = null;
        n = null;
        log.info("run after each test in the class, m={}, n={}", m, n);
    }

    @AfterAll
    public static void tearDownOnce() {
        log.info("run once after all the tests in the class");
    }
}
