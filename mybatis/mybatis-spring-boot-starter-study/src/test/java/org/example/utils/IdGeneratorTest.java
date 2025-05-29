package org.example.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

class IdGeneratorTest {

  @Test
  void testIdFormat() {
    // 测试ID格式
    String id = IdGenerator.nextId().toPlainString();

    // 验证长度（17位时间戳 + 5位序列号 = 22位）
    assertEquals(22, id.length());

    // 验证序列号部分是否为数字
    String sequence = id.substring(17);
    assertTrue(sequence.matches("\\d{5}"));
  }

  @Test
  void testIdUniqueness() throws InterruptedException {
    // 线程数
    int threadCount = 10;
    // 每个线程生成的ID数量
    int idsPerThread = 1000;
    // 存储所有生成的ID
    Set<String> ids = new HashSet<>();

    // 使用CountDownLatch确保所有线程同时开始
    CountDownLatch startLatch = new CountDownLatch(1);
    // 使用CountDownLatch等待所有线程完成
    CountDownLatch endLatch = new CountDownLatch(threadCount);

    // 创建线程池
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

    // 启动多个线程同时生成ID
    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          // 等待开始信号
          startLatch.await();

          // 生成指定数量的ID
          for (int j = 0; j < idsPerThread; j++) {
            String id = IdGenerator.nextId().toPlainString();
            synchronized (ids) {
              ids.add(id);
            }
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        } finally {
          // 标记线程完成
          endLatch.countDown();
        }
      });
    }

    // 发出开始信号
    startLatch.countDown();
    // 等待所有线程完成
    endLatch.await();
    // 关闭线程池
    executorService.shutdown();

    // 验证生成的ID数量是否正确（没有重复）
    assertEquals(threadCount * idsPerThread, ids.size());
  }

  @Test
  void testSequenceReset() {
    // 测试序列号重置功能
    Set<String> sequences = new HashSet<>();
    for (int i = 0; i < 100100; i++) {
      String id = IdGenerator.nextId().toPlainString();
      String sequence = id.substring(17);
      sequences.add(sequence);
    }

    // 验证序列号是否在预期范围内（00000-99999）
    sequences.forEach(seq -> {
      int seqNum = Integer.parseInt(seq);
      assertTrue(seqNum >= 0 && seqNum <= 99999);
    });
  }
}
