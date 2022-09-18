package cn.maiaimei.example.juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁，又称递归锁。同一个线程持有同一把锁，进入内部同步方法或同步代码块时自动获取锁，不出现死锁的情况。
 * synchronized 和 ReentrantLock 都是可重入锁
 * synchronized 是隐式可重入锁
 * ReentrantLock 是显式可重入锁，lock和unlock方法必须成对出现
 */
public class ReentrantLockDemo {
    public static void main(String[] args) {
        test3();
    }

    private static Object lockObj = new Object();
    private static ReentrantLock lock = new ReentrantLock();

    private synchronized void m1() {
        System.out.println(Thread.currentThread().getName() + "\t come in synchronized method m1");
        m2();
        System.out.println(Thread.currentThread().getName() + "\t end");
    }

    private synchronized void m2() {
        System.out.println(Thread.currentThread().getName() + "\t come in synchronized method m2");
        m3();
    }

    private synchronized void m3() {
        System.out.println(Thread.currentThread().getName() + "\t come in synchronized method m3");
    }

    private static void test1() {
        new Thread(() -> {
            synchronized (lockObj) {
                System.out.println(Thread.currentThread().getName() + "\t 进入外层同步代码块");
                synchronized (lockObj) {
                    System.out.println(Thread.currentThread().getName() + "\t 进入中层同步代码块");
                    synchronized (lockObj) {
                        System.out.println(Thread.currentThread().getName() + "\t 进入内层同步代码块");
                    }
                }
            }
        }, "t1").start();
    }

    private static void test2() {
        ReentrantLockDemo reentrantLockDemo = new ReentrantLockDemo();
        new Thread(() -> reentrantLockDemo.m1(), "t2").start();
    }

    private static void test3() {
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t 进入外层同步代码块");
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "\t 进入中层同步代码块");
                    lock.lock();
                    try {
                        System.out.println(Thread.currentThread().getName() + "\t 进入内层同步代码块");
                    } finally {
                        lock.unlock();
                    }
                } finally {
                    lock.unlock();
                }
            } finally {
                lock.unlock();
            }
        }, "t3").start();
    }
}
