package org.example.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * ID生成工具类 格式：yyyyMMddHHmmssSSS + 5位序号（00000-99999） 示例：2024021612301234500001
 */
@Slf4j
public class IdGenerator {

  // 日期格式化
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

  // 序号位数
  private static final int SEQUENCE_LENGTH = 5;

  // 序号最大值 99999
  private static final int MAX_SEQUENCE = (int) Math.pow(10, SEQUENCE_LENGTH) - 1;

  // 原子计数器
  private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

  // 上次生成ID的时间戳
  private static volatile String lastTimestamp = "";

  // 同步锁
  private static final Object LOCK = new Object();

  /**
   * 生成ID
   */
  public static BigDecimal nextId() {
    String timestamp = LocalDateTime.now().format(FORMATTER);

    synchronized (LOCK) {
      // 如果是新的时间戳，重置序号
      if (!timestamp.equals(lastTimestamp)) {
        SEQUENCE.set(0);
        lastTimestamp = timestamp;
      }

      // 获取序号
      int sequence = SEQUENCE.incrementAndGet();

      // 序号超过最大值，等待下一毫秒
      if (sequence > MAX_SEQUENCE) {
        return waitAndGetNextId();
      }

      // 格式化序号
      String sequenceStr = String.format("%0" + SEQUENCE_LENGTH + "d", sequence);

      return new BigDecimal(timestamp + sequenceStr);
    }
  }

  /**
   * 等待下一毫秒并生成ID
   */
  private static BigDecimal waitAndGetNextId() {
    try {
      Thread.sleep(1);
    } catch (InterruptedException e) {
      log.error("IdGenerator interrupted", e);
      Thread.currentThread().interrupt();
    }
    return nextId();
  }

  /**
   * 批量生成ID
   */
  public static List<BigDecimal> nextIds(int size) {
    Assert.isTrue(size > 0, "Size must be positive");
    List<BigDecimal> ids = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      ids.add(nextId());
    }
    return ids;
  }
}
