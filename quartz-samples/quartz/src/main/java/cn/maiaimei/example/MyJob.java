package cn.maiaimei.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Job是Quartz中的一个接口，接口下只有execute方法，在这个方法中编写业务逻辑。
 */
@Slf4j
public class MyJob implements Job {

  /**
   * 这个方法中编写业务逻辑
   *
   * @param jobExecutionContext jobExecutionContext中包含了Quartz运行时的环境以及Job本身的详细数据信息。
   * @throws JobExecutionException
   */
  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    log.info("execute MyJob at {}, k1={}, k2={}",
        format.format(new Date()),
        jobExecutionContext.getJobDetail().getJobDataMap().get("k1"),
        jobExecutionContext.getTrigger().getJobDataMap().get("k2")
    );
  }
}
