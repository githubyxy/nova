package com.yxy.nova.mwh.utils.concurrent;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: renshui
 * @date: 2020-05-28 10:24 下午
 */
public class BoundedExecutor extends ThreadPoolExecutor {

    private final Semaphore semaphore;

    public BoundedExecutor(int bound) {
        super(bound, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        semaphore = new Semaphore(bound);
    }

    public BoundedExecutor() {
        this(Runtime.getRuntime().availableProcessors() * 2);
    }

    /**Submits task to execution pool, but blocks while number of running threads
     * has reached the bound limit
     */
    public <T> Future<T> submitButBlockIfFull(final Callable<T> task) throws InterruptedException{
        semaphore.acquire();
        return submit(task);
    }


    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        semaphore.release();
    }
}
