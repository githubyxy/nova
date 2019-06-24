package multithreading.threadDemo;

/**
 * 继承Thread，重写父类的run()方法。master
 *
 * @author yuxiaoyu
 */
public class Mythread extends Thread {

    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + "在运行!");

        }
    }

}
