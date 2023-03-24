package cn.maiaimei.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * TODO：使用 @ExtendWith + @ContextConfiguration，日志级别为什么不生效？
 *
 * @ExtendWith(SpringExtension.class)
 * @ContextConfiguration(classes = TestContextConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
 */
@SpringBootTest
class LogTest {
    private static final Logger log = LoggerFactory.getLogger(LogTest.class);

    @Test
    void trace() {
        log.trace("trace 1");
        if (log.isTraceEnabled()) {
            log.trace("trace 2");
        }
    }

    @Test
    void debug() {
        log.debug("debug 1");
        if (log.isDebugEnabled()) {
            log.debug("debug 2");
        }
    }

    @Test
    void info() {
        log.info("info 1");
        if (log.isInfoEnabled()) {
            log.info("info 2");
        }
    }

    @Test
    void warn() {
        log.warn("warn 1");
        if (log.isWarnEnabled()) {
            log.warn("warn 2");
        }
    }

    @Test
    void error() {
        log.error("error 1");
        if (log.isErrorEnabled()) {
            log.error("error 2");
        }
    }
}
