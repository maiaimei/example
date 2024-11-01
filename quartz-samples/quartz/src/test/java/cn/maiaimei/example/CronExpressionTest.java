package cn.maiaimei.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.CronExpression;

/**
 * cron表达式是一个字符串，由6到7个字段组成，用空格分隔。其中前6个字段是必须的，最后一个是可选的。
 * <p>
 * 秒 分 时 日 月 周 年
 * <p>
 * 【*】：每的意思。在不同的字段上，就代表每秒，每分，每小时等。
 * <p>
 * 【-】：指定值的范围。比如[1-10]，在秒字段里就是每分钟的第1到10秒，在分就是每小时的第1到10分钟，以此类推。
 * <p>
 * 【,】：指定某几个值。比如[2,4,5]，在秒字段里就是每分钟的第2，第4，第5秒，以此类推。
 * <p>
 * 【/】：指定值的起始和增加幅度。比如[3/5]，在秒字段就是每分钟的第3秒开始，每隔5秒生效一次，也就是第3秒、8秒、13秒，以此类推。
 * <p>
 * 【?】：仅用于【日】和【周】字段。因为在指定某日和周几的时候，这两个值实际上是冲突的，所以需要用【?】标识不生效的字段。
 * <p>
 * 周日到周六分别为：SUN，MON，TUE，WED，THU，FRI，SAT，对应数字1，2，3，4，5，6，7
 * <p>
 * https://cron.qqe2.com/
 */
@Slf4j
public class CronExpressionTest {

  @Test
  public void test_cron() throws ParseException {
    // 表示 每 1秒 执行任务
    //print("* * * * * ?");

    // 表示 每 2秒 执行任务
    //print("0/2 * * * * ?");

    // 表示 每 1分钟 执行任务
    //print("0 0/1 * * * ?");

    // 表示 每 15分钟 执行任务
    //print("0 0/15 * * * ?");

    // 表示 每 30分钟 执行任务
    //print("0 0/30 * * * ?");

    // 朝九晚五工作时间内每半小时 
    //print("0 0/30 9-17 * * ?");

    // 表示 每 1小时 执行任务
    //print("0 0 0/1 * * ?");

    // 表示 每小时的第5分钟触发，每 1小时 执行任务
    print("0 5 0/1 * * ?");

    // 表示 周一到周六7点到21点每小时的第5分钟触发 执行任务
    //print("0 5 7-21 ? * MON-SAT");

    // 表示 每月的1日的凌晨2点执行任务
    //print("0 0 2 1 * ?");

    // 表示 每天上午10点，下午2点，4点 执行任务
    //print("0 0 10,14,16 * * ?");
  }

  @Test
  public void test_tolerace() throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date now = format.parse("2023-11-05 07:45:00");
    Date nextValidTime = format.parse("2023-11-05 08:15:00");
    long milliseconds = nextValidTime.getTime() - now.getTime();
    long seconds = milliseconds / 1000;
    log.info("max tolerace is: {}", seconds);

    long tolerace = 100 * 1000;
    log.info("tolerace is: {}", tolerace);

    final Calendar calendar = Calendar.getInstance();
    while (true) {
      if (nextValidTime.getTime() - now.getTime() > tolerace) {
        log.info("{}\t{}\t{}\t{}",
            format.format(now),
            format.format(nextValidTime.getTime()),
            nextValidTime.getTime() - now.getTime(),
            nextValidTime.getTime() - now.getTime() > tolerace
        );
      }
      calendar.setTime(now);
      calendar.add(Calendar.MINUTE, 1);
      now = calendar.getTime();
    }
  }

  private void print(String cron) throws ParseException {
    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    CronExpression cronExpression = new CronExpression(cron);
    Date date = new Date();
    for (int i = 0; i < 100; i++) {
      date = cronExpression.getNextValidTimeAfter(date);
      log.info("{}", format.format(date));
    }
  }
}
