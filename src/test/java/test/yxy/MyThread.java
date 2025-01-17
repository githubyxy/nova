package test.yxy;

import lombok.SneakyThrows;

public class MyThread extends Thread {

    /** 该线程存活标志，kill()方法将该标志置为false。*/
    private boolean alive = true;

    @SneakyThrows
    public final void run() {
        while (alive) {
            System.out.println("MyThread 执行中……");
            Thread.sleep(1000);
        }
    }


    public void kill() {
        alive = false;
    }

}
