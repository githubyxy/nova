package multithreading.singtonDemo;

public class MyThread6 extends Thread {

    public void run() {
        System.out.println(MySingletonObject6.A.getInstance().hashCode());
    }
}
