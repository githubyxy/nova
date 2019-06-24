package multithreading.synchronizedDemo;

public class Mythread3_1 extends Thread {
    @Override
    public void run() {
        MyDomain3_1_Son sub = new MyDomain3_1_Son();
        sub.operateISubMethod();
    }

}
