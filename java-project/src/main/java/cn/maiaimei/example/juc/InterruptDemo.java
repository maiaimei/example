package cn.maiaimei.example.juc;

import java.util.concurrent.TimeUnit;

public class InterruptDemo {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + "\t is end");
                    break;
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "\t is running");
            }
        }, "t1");
        t1.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            t1.interrupt();
        }, "t2").start();
    }
}
