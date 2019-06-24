package testMethod.ThreadTest;

import java.util.concurrent.Semaphore;

public class SemphoreTest {

    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(5);

        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "获得了信号量,时间为" + System.currentTimeMillis());
                    Thread.sleep(2000);
                    System.out.println(Thread.currentThread().getName() + "释放了信号量,时间为" + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // seamphore调用acquire后，必须有对应的release
                    semaphore.release();
                }
            }
        };

        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++)
            threads[i] = new Thread(runnable);
        for (int i = 0; i < threads.length; i++)
            threads[i].start();
    }
}
