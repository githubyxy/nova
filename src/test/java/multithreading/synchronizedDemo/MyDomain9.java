package multithreading.synchronizedDemo;

/**
 * synchronized同步方法 与 synchronized(this)同步代码块
 *
 * @author yuxiaoyu
 */
public class MyDomain9 {

    public void serviceMethodA() {
        synchronized (this) {
            try {
                System.out.println("A begin time = " + System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.println("A end time = " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    synchronized public void serviceMethodB() {
        System.out.println("B begin time = " + System.currentTimeMillis());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("B end time = " + System.currentTimeMillis());
    }
}
