package multithreading.synchronizedDemo;

public class MyThread9_2 extends Thread {

    private MyDomain9 td;

    public MyThread9_2(MyDomain9 td) {
        this.td = td;
    }

    public void run() {
        td.serviceMethodA();
    }

}
