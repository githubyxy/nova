package multithreading.threadDemo;

/**
 * 实现Runnable接口。和继承自Thread类差不多，不过实现Runnable后，还是要通过一个Thread来启动：
 *
 * @author yuxiaoyu
 */
public class Mythread2 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + "在运行!");
        }
    }

}
