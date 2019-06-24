package multithreading.threadpool;

import java.util.concurrent.Callable;

public class ThreadCallable implements Callable<String> {

    private int i;

    public ThreadCallable(int i) {
        this.i = i;
    }

    @Override
    public String call() {
        if (i % 2 == 0) {
            throw new RuntimeException();
        } else {
            return i + "";
        }
    }

}
