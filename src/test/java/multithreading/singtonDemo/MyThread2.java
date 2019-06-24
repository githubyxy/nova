package multithreading.singtonDemo;

public class MyThread2 extends Thread {

    public void run() {
        System.out.println(MySingletonObject2.getInstance().hashCode());
    }
}
