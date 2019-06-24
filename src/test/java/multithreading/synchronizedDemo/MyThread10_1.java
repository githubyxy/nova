package multithreading.synchronizedDemo;

public class MyThread10_1 extends Thread {

    private MyDomain10 td;

    public MyThread10_1(MyDomain10 td) {
        this.td = td;
    }

    public void run() {
        td.serviceMethodB();
    }

}
