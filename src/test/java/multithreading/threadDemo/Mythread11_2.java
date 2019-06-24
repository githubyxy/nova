package multithreading.threadDemo;

/**
 * interrupted() : 返回运行这个方法的当前线程中断标识位是否中断，为true时并清除中断标识位，即连续两次调用该方法的返回值必定是false
 *
 * @author yuxiaoyu
 */
public class Mythread11_2 extends Thread {

    public void run() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < 50000; i++) {
            if (Thread.interrupted()) {
                System.out.println("interrupted检测到中断了，i=" + i);
            }
        }
    }

}
