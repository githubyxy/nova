package multithreading.synchronizedDemo;


public class Mythread2_1 extends Thread {

    private MyDomain2 object;

    public Mythread2_1(MyDomain2 object) {
        this.object = object;
    }

    @Override
    public void run() {
        object.methodA();
    }

}
