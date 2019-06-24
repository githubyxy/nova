package multithreading.reentrantLockDemo;

public class Mythread3_1 extends Thread {

    private MyDomain3 myDomain3;

    public Mythread3_1(MyDomain3 myDomain3) {
        this.myDomain3 = myDomain3;
    }

    @Override
    public void run() {
        myDomain3.testReadLock();
    }
}
