package multithreading.synchronizedDemo;

public class MyThread8_2 extends Thread {

    private MyDomain8 td;

    public MyThread8_2(MyDomain8 td) {
        this.td = td;
    }

    public void run() {
        td.serviceMethodA();
    }

}
