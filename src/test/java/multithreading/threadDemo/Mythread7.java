package multithreading.threadDemo;

/**
 * 测试isAlive()方法
 *
 * @author yuxiaoyu
 */
public class Mythread7 extends Thread {

    public void run() {
        System.out.println("this.isAlive() =" + this.isAlive());
        System.out.println("Thread.currentThread().isAlive() =" + Thread.currentThread().isAlive());
    }

}
