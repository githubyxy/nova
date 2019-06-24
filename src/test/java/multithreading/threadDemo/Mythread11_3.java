package multithreading.threadDemo;

/**
 * isInterrupted() : 返回调用这个方法的线程中断标识位是否中断，不能清除中断标识位
 *
 * @author yuxiaoyu
 */
public class Mythread11_3 extends Thread {

    public void run() {
        for (int i = 0; i < 8000; i++) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("isInterrupted检测到中断了，i=" + i);
            }
        }
    }

}
