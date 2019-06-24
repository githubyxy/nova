package multithreading.synchronizedDemo;

public class MyThread11_2 extends Thread {

    private MyDomain11 td;

    public MyThread11_2(MyDomain11 td) {
        this.td = td;
    }

    public void run() {
        td.serviceMethodA(td);
    }

}
