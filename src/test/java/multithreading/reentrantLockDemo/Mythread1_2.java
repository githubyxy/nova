package multithreading.reentrantLockDemo;

public class Mythread1_2 extends Thread {

    private MyDomain1 myDomain1;

    public Mythread1_2(MyDomain1 myDomain1) {
        this.myDomain1 = myDomain1;
    }

    @Override
    public void run() {
        myDomain1.method2();
    }
}
