package multithreading.synchronizedDemo;

/**
 * 继承Thread，重写父类的run()方法。
 *
 * @author yuxiaoyu
 */
public class Mythread3 extends Thread {

    public void run() {
        MyDomain3 service = new MyDomain3();
        service.service1();
    }

}
