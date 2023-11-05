package cn.maiaimei.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class MyScheduler {

  public static void main(String[] args)
      throws SchedulerException, InterruptedException, ParseException {
    // 创建Scheduler
    final StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
    final Scheduler scheduler = schedulerFactory.getScheduler();
    // 创建JobDetail，并与MyJob绑定
    // JobDetail定义的是任务数据，而真正的执行逻辑是在Job中。 
    // Sheduler每次执行，都会根据JobDetail创建一个新的Job实例，任务执行结束后，关联的Job实例会被释放，然后被JVM GC清除。这样可以规避Job实例并发访问的问题
    final JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
        .withIdentity("myJob", "my")
        .usingJobData("k1", "v1")
        .build();
    // 创建Trigger，指定Job的执行时间、执行间隔、执行次数
    //final Trigger trigger = createSimpleTrigger();
    final Trigger trigger = createCronTrigger();
    // 执行
    scheduler.scheduleJob(jobDetail, trigger);
    scheduler.start();
    // 睡眠 1分钟
    TimeUnit.MINUTES.sleep(3);
    // 关闭
    scheduler.shutdown();
  }

  private static Trigger createSimpleTrigger() {
    // 创建Trigger，指定Job的执行时间、执行间隔、执行次数
    return TriggerBuilder.newTrigger()
        .withIdentity("mySimpleTrigger", "my")
        .startNow() // 立即生效
        .withSchedule(
            SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(1) // 每隔 1s 执行一次
                .repeatForever() // 一直执行
        )
        .usingJobData("k2", "v2")
        .build();
  }

  private static Trigger createCronTrigger() throws ParseException {
    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final Date startAt = format.parse("2023-11-05 11:06:00");
    final Date endAt = format.parse("2023-11-05 11:07:00");
    // 创建Trigger，指定Job的执行时间、执行间隔、执行次数
    return TriggerBuilder.newTrigger()
        .withIdentity("myCronTrigger", "my")
        .startAt(startAt)
        .endAt(endAt)
        .withSchedule(
            CronScheduleBuilder.cronSchedule("0/1 * * * * ?")
        )
        .usingJobData("k2", "v2")
        .build();
  }
}
