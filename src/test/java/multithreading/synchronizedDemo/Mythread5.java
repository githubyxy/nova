package multithreading.synchronizedDemo;

/**
 * 继承Thread，重写父类的run()方法。
 *
 * @author yuxiaoyu
 */
public class Mythread5 extends Thread {

    private MyDomain5_Son service;

    public Mythread5(MyDomain5_Son myDomain5_Son) {
        this.service = myDomain5_Son;
    }

    public void run() {
        service.testMethod();
    }

}
