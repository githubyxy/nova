package multithreading.reentrantLockDemo;

public class Mythread3_2 extends Thread {

    private MyDomain3 myDomain3;

    public Mythread3_2(MyDomain3 myDomain3) {
        this.myDomain3 = myDomain3;
    }

    @Override
    public void run() {
        myDomain3.testWriteLock();
    }
}
