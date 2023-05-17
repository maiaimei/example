package cn.maiaimei.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.*;

import static org.junit.Assert.assertEquals;

/**
 * JUnit4中测试用例无需继承TestCase类，只需标记@Test注解即可
 * 一个JUnit4的单元测试用例执行顺序为：@BeforeClass -> @Before -> @Test -> @After -> @AfterClass，@BeforeClass和@AfterClass仅执行一次
 */
@Slf4j
public class JUnit4Test {
    private Integer m, n;

    @BeforeClass
    public static void setUpOnce() {
        log.info("run once before any of the test methods in the class");
    }

    @Before
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

    @After
    public void tearDown() {
        m = null;
        n = null;
        log.info("run after each test in the class, m={}, n={}", m, n);
    }

    @AfterClass
    public static void tearDownOnce() {
        log.info("run once after all the tests in the class");
    }
}
