package multithreading.synchronizedDemo;

public class MyThread7_2 extends Thread {

    private MyDomain7 task;

    public MyThread7_2(MyDomain7 task) {
        super();
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
        task.doLongTimeTask();
    }

}
