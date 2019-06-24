package multithreading.synchronizedDemo;

/**
 * synchronized同步方法 与 synchronized(非this的自身对象x)同步代码块 可以同时进入
 *
 * @author yuxiaoyu
 */
public class MyDomain11 {

    public void serviceMethodA(MyDomain11 anyString) {
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
//	public void serviceMethodB() {
//		synchronized (this) {
//			try {
//				System.out.println("B begin time = " + System.currentTimeMillis());
//				Thread.sleep(2000);
//				System.out.println("B end time = " + System.currentTimeMillis());
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
