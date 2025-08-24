package org.example.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.example.config.JacksonConfig;
import org.springframework.util.StringUtils;

/**
 * <p> 使用 JVM 参数 `-Duser.timezone` 启动程序，例如： java -Duser.timezone=UTC MainClass， 代码无需特别处理日期时间
 * <p> 使用 {@link JacksonConfig} 统一处理日期时间序列化与反序列化
 */
public final class DateTimeUtils {

  private DateTimeUtils() {
    throw new UnsupportedOperationException();
  }

  // 获取当前UTC时间
  public static LocalDateTime getCurrentUtcTime() {
    return LocalDateTime.now(ZoneOffset.UTC);
  }

  // 本地时间转UTC
  public static LocalDateTime toUtc(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    return localDateTime.atZone(ZoneId.systemDefault())
        .withZoneSameInstant(ZoneOffset.UTC)
        .toLocalDateTime();
  }

  // UTC转本地时间
  public static LocalDateTime toLocal(LocalDateTime utcDateTime) {
    if (utcDateTime == null) {
      return null;
    }
    return utcDateTime.atZone(ZoneOffset.UTC)
        .withZoneSameInstant(ZoneId.systemDefault())
        .toLocalDateTime();
  }

  // 格式化UTC时间
  public static String formatUtcTime(LocalDateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
  }

  // 解析UTC时间字符串
  public static LocalDateTime parseUtcTime(String dateTimeStr) {
    if (StringUtils.hasText(dateTimeStr)) {
      return null;
    }
    return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_DATE_TIME);
  }
}
