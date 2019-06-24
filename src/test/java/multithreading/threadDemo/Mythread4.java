package multithreading.threadDemo;

/**
 * 测试Thread.currentThread()：返回代码段正在被哪个线程调用的信息
 * this 始终表示线程实例本身
 *
 * @author yuxiaoyu
 */
public class Mythread4 extends Thread {
    public Mythread4() {
        System.out.println("Mythread4构造方法：Thread.currentThread().getName()=" + Thread.currentThread().getName());
        System.out.println("Mythread4构造方法：this.getName()=" + this.getName());
    }

    public void run() {
        System.out.println("run 方法：Thread.currentThread().getName()=" + Thread.currentThread().getName());
        System.out.println("run 方法：this.getName()=" + this.getName());
    }

}
