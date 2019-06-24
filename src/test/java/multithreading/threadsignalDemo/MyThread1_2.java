package multithreading.threadsignalDemo;

public class MyThread1_2 extends Thread {

    private Object lock;

    public MyThread1_2(Object lock) {
        this.lock = lock;
    }

    public void run() {
        try {
            synchronized (lock) {
                System.out.println("开始------notify time = " + System.currentTimeMillis());
                lock.notify();
                System.out.println("结束------notify time = " + System.currentTimeMillis());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
