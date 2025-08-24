package org.example.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {

  /**
   * 自增序列的最大值（5位数）
   */
  private static final int SEQUENCE_MAX = 99999;

  /**
   * 自增序列的格式化模板（5位，不足补0）
   */
  private static final String SEQUENCE_FORMAT = "%05d";

  /**
   * 日期时间格式
   */
  private static final String DATE_TIME_FORMAT = "yyyyMMddHHmmssSSS";

  /**
   * 日期时间格式化器
   */
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

  /**
   * 原子性的自增序列
   */
  private static final AtomicInteger sequence = new AtomicInteger(0);

  /**
   * 私有构造函数，防止实例化
   */
  private IdGenerator() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * 生成下一个ID 格式：yyyyMMddHHmmsSSS + 5位自增序列
   *
   * @return 生成的ID
   */
  public static BigDecimal nextId() {
    // 获取当前时间戳部分
    String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);

    // 获取自增序列
    int currentSequence = sequence.updateAndGet(current -> {
      // 如果达到最大值，重置为0
      if (current >= SEQUENCE_MAX) {
        return 0;
      }
      return current + 1;
    });

    // 格式化自增序列为5位数字
    String sequenceStr = String.format(SEQUENCE_FORMAT, currentSequence);

    // 组合时间戳和序列号
    return new BigDecimal(timestamp + sequenceStr);
  }
}
