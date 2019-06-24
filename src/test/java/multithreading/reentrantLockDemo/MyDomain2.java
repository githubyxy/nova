package multithreading.reentrantLockDemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyDomain2 {

    private Lock lock = new ReentrantLock();
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();

    public void await() {
        System.out.println("进入await方法");
        try {
            lock.lock();
            for (int i = 0; i < 10; i++) {
                System.out.println("****");
            }
            System.out.println(Thread.currentThread().getName() + " conditionA await");
            conditionA.await();
            System.out.println(Thread.currentThread().getName() + " conditionA await out");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void await2() {
        System.out.println("进入await2方法");
        try {
            lock.lock();
            conditionB.await();
            System.out.println(Thread.currentThread().getName() + " conditionB await");
            Thread.sleep(1000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signalAll() {
        System.out.println("进入signalAll方法");
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " conditionA signalAll");
            conditionA.signalAll();
            System.out.println(Thread.currentThread().getName() + " conditionA signalAll");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signalAll2() {
        System.out.println("进入signalAll2方法");
        try {
            lock.lock();
            conditionB.signalAll();
            System.out.println(Thread.currentThread().getName() + " conditionB signalAll2");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
