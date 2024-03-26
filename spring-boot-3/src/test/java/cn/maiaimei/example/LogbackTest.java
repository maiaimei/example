package cn.maiaimei.example;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTest {

  @Test
  public void test() {
    Logger logger = LoggerFactory.getLogger(LogbackTest.class);
    logger.trace("trace");
    logger.debug("debug");
    logger.warn("warn");
    logger.info("info");
    logger.error("error");
  }

}
