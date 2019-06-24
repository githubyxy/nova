package multithreading.singtonDemo;

public class MyThread1 extends Thread {

    public void run() {
        System.out.println(MySingletonObject1.getInstance().hashCode());
    }
}
