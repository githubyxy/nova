package multithreading.reentrantLockDemo;

public class Mythread2_2 extends Thread {

    private MyDomain2 myDomain2;

    public Mythread2_2(MyDomain2 myDomain2) {
        this.myDomain2 = myDomain2;
    }

    @Override
    public void run() {
        myDomain2.signalAll();
    }
}
