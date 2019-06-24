package multithreading.synchronizedDemo;

/**
 * 死锁
 *
 * @author yuxiaoyu
 */
public class Mythread13 extends Thread {

    public Object lock1 = new Object();
    public Object lock2 = new Object();


    @Override
    public void run() {
        synchronized (lock1) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            synchronized (lock2) {
                System.out.println("按lock1->lock2顺序执行");
            }
        }
        synchronized (lock2) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            synchronized (lock1) {
                System.out.println("按lock2->lock1顺序执行");
            }
        }
    }

}
