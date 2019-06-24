package multithreading.reentrantLockDemo;

import org.junit.Test;

public class ReentrantLockDemoTest {

    @Test
    public void test1() throws InterruptedException {
        MyDomain1 myDomain1 = new MyDomain1();
        Mythread1_1 a = new Mythread1_1(myDomain1);
        Mythread1_2 b = new Mythread1_2(myDomain1);
        Mythread1_1 c = new Mythread1_1(myDomain1);
        Mythread1_1 d = new Mythread1_1(myDomain1);
        a.start();
        b.start();
//		c.start();
//		d.start();

        a.join();
        b.join();
//		c.join();
//		d.join();
    }

    @Test
    public void test2() throws InterruptedException {
        MyDomain2 myDomain2 = new MyDomain2();
        Mythread2_1 a = new Mythread2_1(myDomain2);
        Mythread2_2 b = new Mythread2_2(myDomain2);
        a.start();
        Thread.sleep(1000);
        b.start();

        a.join();
        b.join();

    }

    /**
     * 读锁与读锁之间可以共享
     * 读锁与写锁互斥
     * 写锁与写锁互斥
     *
     * @throws InterruptedException
     */
    @Test
    public void test3() throws InterruptedException {
        MyDomain3 myDomain3 = new MyDomain3();
        Mythread3_1 readLock = new Mythread3_1(myDomain3);
        Mythread3_1 readLock2 = new Mythread3_1(myDomain3);
        Mythread3_2 writeLock = new Mythread3_2(myDomain3);
        Mythread3_2 writeLock2 = new Mythread3_2(myDomain3);

//		readLock.start();
//		readLock2.start();
        writeLock.start();
        writeLock2.start();

        Thread.sleep(3000);
    }


}
