package multithreading.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {


    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, Runtime.getRuntime().availableProcessors() * 2, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));
        List<Future<String>> futureList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            futureList.add(executor.submit(new ThreadCallable(i)));
        }

        for (Future<String> future : futureList) {
            try {
                System.out.println(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
