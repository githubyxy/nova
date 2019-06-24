package multithreading.threadDemo;

/**
 * 测试isAlive()方法
 *
 * @author yuxiaoyu
 */
public class Mythread7_2 extends Thread {

    public Mythread7_2() {
        System.out.println("Mythread7_2---begin");

        System.out.println("Thread.currentThread().getName()=" + Thread.currentThread().getName());
        System.out.println("Thread.currentThread().isAlive()=" + Thread.currentThread().isAlive());

        System.out.println("this.getName()=" + this.getName());
        System.out.println("this.isAlive()=" + this.isAlive());

        System.out.println("Mythread7_2---end");
    }

    @Override
    public void run() {
        System.out.println("run---begin");

        System.out.println("Thread.currentThread().getName()=" + Thread.currentThread().getName());
        System.out.println("Thread.currentThread().isAlive()=" + Thread.currentThread().isAlive());

        System.out.println("this.getName()=" + this.getName());
        System.out.println("this.isAlive()=" + this.isAlive());

        System.out.println("run---end");
    }

}
