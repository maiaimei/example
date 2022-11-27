package cn.maiaimei.example.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * https://www.bilibili.com/video/BV1ar4y1x727?p=50
 */
public class LockSupportDemo {
    public static void main(String[] args) {
        //testSynchronizedWaitNotify3();
        //testLockAwaitSignal3();
        testLockSupportParkUnpark2();
    }

    private static void testLockSupportParkUnpark1() {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
        }, "t1");
        t1.start();

        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
            LockSupport.unpark(t1);
            System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
        }, "t2").start();
    }

    private static void testLockSupportParkUnpark2() {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
        }, "t1");
        t1.start();

//        try {
//            TimeUnit.SECONDS.sleep(1L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
            LockSupport.unpark(t1);
            System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
        }, "t2").start();
    }

    /**
     * Lock + await + signal
     * <p>await和signal必须放在lock()和unlock()中</p>
     * <p>先await后signal</p>
     */
    private static void testLockAwaitSignal1() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }

    /**
     * Lock + await + signal
     * <p>await和signal必须放在lock()和unlock()中</p>
     */
    private static void testLockAwaitSignal2() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            //lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
            } finally {
                //lock.unlock();
            }
        }, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            //lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
            } finally {
                //lock.unlock();
            }
        }, "t2").start();
    }

    /**
     * Lock + await + signal
     * <p>先await后signal</p>
     */
    private static void testLockAwaitSignal3() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
            } finally {
                lock.unlock();
            }
        }, "t1").start();

//        try {
//            TimeUnit.SECONDS.sleep(1L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }

    /**
     * synchronized + wait + notify
     * <p>wait和notify必须放在同步方法或同步代码块中</p>
     * <p>先wait后notify</p>
     */
    private static void testSynchronizedWaitNotify1() {
        Object lockObject = new Object();

        new Thread(() -> {
            synchronized (lockObject) {
                System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
                try {
                    lockObject.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
            }
        }, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
            synchronized (lockObject) {
                lockObject.notify();
            }
            System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
        }, "t2").start();
    }

    /**
     * synchronized + wait + notify
     * <p>wait和notify必须放在同步方法或同步代码块中</p>
     */
    private static void testSynchronizedWaitNotify2() {
        Object lockObject = new Object();

        new Thread(() -> {
            //synchronized (lockObject) {
            System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
            try {
                lockObject.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
            //}
        }, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
            //synchronized (lockObject) {
            lockObject.notify();
            //}
            System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
        }, "t2").start();
    }

    /**
     * synchronized + wait + notify
     * <p>先wait后notify</p>
     */
    private static void testSynchronizedWaitNotify3() {
        Object lockObject = new Object();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lockObject) {
                System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
                try {
                    lockObject.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
            }
        }, "t1").start();

//        try {
//            TimeUnit.SECONDS.sleep(1L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        new Thread(() -> {
            synchronized (lockObject) {
                System.out.println(Thread.currentThread().getName() + "\tbegin\t" + System.currentTimeMillis());
                lockObject.notify();
                System.out.println(Thread.currentThread().getName() + "\tend\t" + System.currentTimeMillis());
            }
        }, "t2").start();
    }
}
