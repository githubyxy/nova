package multithreading.reentrantLockDemo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyDomain1 {

    private Lock lock = new ReentrantLock();

    public void method1() {
        System.out.println("进入method1方法");
        try {
            lock.lock();
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " i=" + i);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // 为了测试 lock 和 synchronized同步方法不是同一把锁
    public synchronized void method2() {
        System.out.println("进入method2方法");
        for (int j = 0; j < 5; j++) {
            System.out.println(Thread.currentThread().getName() + " j=" + j);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
