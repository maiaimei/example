package cn.maiaimei.example;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * https://blog.51cto.com/u_14122613/4780220
 * <p>
 * https://www.cnblogs.com/yourbatman/p/14307194.html
 * <p>
 * https://cloud.tencent.com/developer/article/2304208
 * <p>
 * https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html
 */
@Slf4j
public class DateTest {

  @Test
  public void printZoneId() {
    // 获取所有时区，输出：[Asia/Aden, America/Cuiaba, Etc/GMT+9, Etc/GMT+8, Africa/Nairobi,……
    Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
    List<String> sortedAvailableZoneIds = availableZoneIds.stream().sorted().toList();
    log.info("所有时区：");
    for (String sortedAvailableZoneId : sortedAvailableZoneIds) {
      log.info("{}", sortedAvailableZoneId);
    }
    // 获取当前计算机默认的时区，也就是东八区时间，输出：Asia/Shanghai
    log.info("JDK8之前获取当前计算机的时区：{}", TimeZone.getDefault());
    log.info("JDK8之后获取当前计算机的时区：{}", ZoneId.systemDefault());
  }

  @Test
  public void getZoneId() {
    // 本地时间
    log.info("{}", TimeZone.getTimeZone("GMT+08:00").getID());
    log.info("{}", TimeZone.getDefault().getID());

    // 纽约时间
    log.info("{}", TimeZone.getTimeZone("GMT-05:00").getID());
    log.info("{}", TimeZone.getTimeZone("America/New_York").getID());

    log.info("{}", TimeZone.getTimeZone("UTC+01:00").getID());
  }

  @Test
  public void timeZoneConversion() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 获取当前的本地时间
    ZonedDateTime now = ZonedDateTime.now();
    log.info("本地时间: " + now.format(formatter));

    // 转换为纽约时区
    ZonedDateTime nyTime = now.withZoneSameInstant(ZoneId.of("America/New_York"));
    log.info("纽约时间: " + nyTime.format(formatter));

    // 转换为格林尼治时间时区
    ZonedDateTime gmtTime = now.withZoneSameInstant(ZoneId.of("GMT"));
    log.info("格林尼治时间: " + gmtTime.format(formatter));
  }

  @Test
  public void formatDateTime() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    ZonedDateTime now = ZonedDateTime.now();
    String formattedNow = now.format(formatter);
    log.info("格式化前的时间: {}", now);
    log.info("格式化后的时间: {}", formattedNow);
  }
}
