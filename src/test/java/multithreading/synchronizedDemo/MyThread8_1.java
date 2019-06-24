package multithreading.synchronizedDemo;

public class MyThread8_1 extends Thread {

    private MyDomain8 td;

    public MyThread8_1(MyDomain8 td) {
        this.td = td;
    }

    public void run() {
        td.serviceMethodB();
    }

}
