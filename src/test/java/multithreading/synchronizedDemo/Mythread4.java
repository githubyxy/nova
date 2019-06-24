package multithreading.synchronizedDemo;

/**
 * 继承Thread，重写父类的run()方法。
 *
 * @author yuxiaoyu
 */
public class Mythread4 extends Thread {

    private MyDomain4 service;

    public Mythread4(MyDomain4 service) {
        this.service = service;
    }

    public void run() {
        service.testMethod();
    }

}
