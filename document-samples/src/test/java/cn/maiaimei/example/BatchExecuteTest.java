package cn.maiaimei.example;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class BatchExecuteTest {

  private static final Random random = new Random();

  public static void main(String[] args) {
    int size = 10;
    List<Integer> largeList = Lists.newArrayList();
    for (int i = 1; i <= random.nextInt(1, 1000); i++) {
      largeList.add(i);
    }

    log.info("本机CPU核心数：{}", Runtime.getRuntime().availableProcessors());
    log.info("更新任务开始, 数据量：{}", largeList.size());
    long startTime = System.nanoTime();
    if (largeList.size() > size) {
      // 初始化线程池
      ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 50,
          4, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
          new ThreadPoolExecutor.CallerRunsPolicy());
      try {
        // 大集合拆分成N个小集合，用多线程并发处理N个小集合
        List<List<Integer>> partitionedLists = Lists.partition(largeList, size);
        // 记录任务个数
        CountDownLatch countDownLatch = new CountDownLatch(partitionedLists.size());
        for (List<Integer> subList : partitionedLists) {
          // 线程池执行
          threadPool.execute(new Thread(() -> {
            // 其他业务逻辑
            log.info("当前工作线程：{}, 本次批量更新数据量：{}",
                Thread.currentThread().getName(), subList.size());
            // 任务个数 - 1, 直至为0时唤醒await()
            countDownLatch.countDown();
          }));
        }
        // 让当前线程处于阻塞状态，直到锁存器计数为零
        countDownLatch.await();
      } catch (Exception e) {
        log.error("系统出现异常", e);
      } finally {
        threadPool.shutdown();
      }
    } else {
      log.info("当前工作线程：{}, 更新数据量：{}",
          Thread.currentThread().getName(), largeList.size());
    }
    long endTime = System.nanoTime();
    log.info("更新任务结束，共计用时{}毫秒",
        TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
  }

  @Test
  public void testListsPartition() {
    List<Integer> largeList = Lists.newArrayList();
    for (int i = 1; i <= 15; i++) {
      largeList.add(i);
    }
    // 每个子列表包含10个元素
    int partitionSize = 10;
    List<List<Integer>> partitionedLists = Lists.partition(largeList, partitionSize);
    for (List<Integer> subList : partitionedLists) {
      System.out.println(subList);
    }
  }

}
