package multithreading.singtonDemo;

public class MyThread3 extends Thread {

    public void run() {
        System.out.println(MySingletonObject3.getInstance().hashCode());
    }
}
