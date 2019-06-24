package multithreading.threadDemo;

/**
 * 测试count 实例变量 共享问题
 *
 * @author yuxiaoyu
 */
public class Mythread3_2 extends Thread {
    private int count = 5;

    public void run() {
        count--;
        System.out.println(Thread.currentThread().getName() + "当前count值为：" + count);
    }

}
