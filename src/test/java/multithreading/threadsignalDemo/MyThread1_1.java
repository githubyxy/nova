package multithreading.threadsignalDemo;

public class MyThread1_1 extends Thread {

    private Object lock;

    public MyThread1_1(Object lock) {
        this.lock = lock;
    }

    public void run() {
        try {
            synchronized (lock) {
                System.out.println("开始------wait time = " + System.currentTimeMillis());
                lock.wait();
                System.out.println("结束------wait time = " + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
