package multithreading.synchronizedDemo;


public class Mythread12_3 extends Thread {

    private MyDomain12 td;

    public Mythread12_3(MyDomain12 td) {
        this.td = td;
    }

    public void run() {
        td.printC();
    }

}
