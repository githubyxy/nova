package multithreading.synchronizedDemo;

public class MyThread10_2 extends Thread {

    private MyDomain10 td;

    public MyThread10_2(MyDomain10 td) {
        this.td = td;
    }

    public void run() {
        td.serviceMethodA();
    }

}
