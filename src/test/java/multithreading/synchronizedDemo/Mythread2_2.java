package multithreading.synchronizedDemo;


public class Mythread2_2 extends Thread {

    private MyDomain2 object;

    public Mythread2_2(MyDomain2 object) {
        this.object = object;
    }

    @Override
    public void run() {
        object.methodB();
    }

}
