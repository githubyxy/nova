package multithreading.synchronizedDemo;

/**
 * synchronized同步方法 与 synchronized(非this非自身其他对象x)同步代码块 可以同时进入
 *
 * @author yuxiaoyu
 */
public class MyDomain10 {

    private String anyString = new String();

    public void serviceMethodA() {
        synchronized (anyString) {
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
        try {
            System.out.println("B begin time = " + System.currentTimeMillis());
            Thread.sleep(2000);
            System.out.println("B end time = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
