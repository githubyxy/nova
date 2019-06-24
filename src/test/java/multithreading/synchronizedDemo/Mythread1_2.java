package multithreading.synchronizedDemo;

public class Mythread1_2 extends Thread {

    private MyDomain1 numRef;

    public Mythread1_2(MyDomain1 numRef) {
        super();
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("b");
    }

}
