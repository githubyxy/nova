package testMethod.ThreadTest;

import java.util.concurrent.locks.ReentrantLock;

public class MyThread implements Runnable {

    private static ReentrantLock lock = new ReentrantLock();

    private static Object obj = new Object();

    private String threadName;

    public MyThread(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void run() {
        // 测试2  interrupt()  join()
        if ("线程1".equals(threadName)) {
            try {
                Thread.currentThread().join(3 * 1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Thread.currentThread().interrupt();
            for (int i = 0; i < 3; i++) {
                Thread.currentThread().interrupt();
//					System.out.println(Thread.currentThread().getName() + Thread.currentThread().isInterrupted());
                System.out.println(Thread.currentThread().getName() + Thread.currentThread().interrupted());
            }

        } else {
            synchronized (obj) {
                System.out.println(threadName + "获取到了锁");
            }
        }


        // 测试1  wait() notify() notifyAll()
		/*if (threadName.startsWith("wait")) {
			try {
				synchronized (obj) {
					System.out.println(threadName + " wait at " + System.currentTimeMillis());
					obj.wait();
					System.out.println(threadName + " run  at " + System.currentTimeMillis());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			synchronized (obj) {
				obj.notifyAll();
			}
			}*/
    }


}
