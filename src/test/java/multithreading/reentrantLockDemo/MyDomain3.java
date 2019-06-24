package multithreading.reentrantLockDemo;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyDomain3 {

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void testReadLock() {
        try {
            lock.readLock().lock();
            System.out.println(System.currentTimeMillis() + " 获取读锁");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void testWriteLock() {
        try {
            lock.writeLock().lock();
            System.out.println(System.currentTimeMillis() + " 获取写锁");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

}
