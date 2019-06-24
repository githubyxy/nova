package multithreading.threadDemo;

/**
 * 测试 interrupt 方法
 *
 * @author yuxiaoyu
 */
public class Mythread11 extends Thread {

    public void run() {
        for (int i = 0; i < 50000; i++) {
            System.out.println("i=" + i);
        }
    }

}
