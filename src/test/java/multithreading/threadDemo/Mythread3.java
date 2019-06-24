package multithreading.threadDemo;

/**
 * 测试count 实例变量 共享问题
 *
 * @author yuxiaoyu
 */
public class Mythread3 extends Thread {
    private int count = 5;

    public Mythread3(String name) {
        this.setName(name);
    }

    public void run() {
        while (count > 0) {
            count--;
            System.out.println(Thread.currentThread().getName() + "当前count值为：" + count);
        }
    }

}
