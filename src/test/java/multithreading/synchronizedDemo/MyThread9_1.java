package multithreading.synchronizedDemo;

public class MyThread9_1 extends Thread {

    private MyDomain9 td;

    public MyThread9_1(MyDomain9 td) {
        this.td = td;
    }

    public void run() {
        td.serviceMethodB();
    }

}
