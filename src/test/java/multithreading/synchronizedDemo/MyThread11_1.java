package multithreading.synchronizedDemo;

public class MyThread11_1 extends Thread {

    private MyDomain11 td;

    public MyThread11_1(MyDomain11 td) {
        this.td = td;
    }

    public void run() {
        td.serviceMethodB();
    }

}
