package com.yxy.nova.mwh.kafka.util;


import com.yxy.nova.mwh.kafka.tape2.QueueFile;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueTest {


    public static void main(String[] argv) throws Exception {
        final QueueFile qf = new QueueFile.Builder(new File("/tmp/module-kafka-q1.bin")).build();
        Random random = new Random();

        final AtomicInteger counter = new AtomicInteger();

        final Thread poller = new Thread() {
            @Override
            public void run() {
                int total = 0;
                int cnt = 0;
                int err = 0;
                System.out.println("now start peeking");
                while (true) {
                    try {
                        byte[] data = qf.peek();
                        if (data == null) {
                            System.out.printf("polled completed with cnt:%d, err %d\n", cnt, err);
                            break;
                        } else {
                            cnt++;
                            qf.remove();
                            total += data.length;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    } catch (NegativeArraySizeException e) {
                        err++;
                    }
                }
            }
        };

        for (int i = 0; i < 1000; i++) {
            byte[] bb = new byte[1000+i];
            random.nextBytes(bb);
            qf.add(bb);
            counter.addAndGet(bb.length);
            Thread.sleep(10);
            if (i == 500) {
                //poller.start();
            }
        }
        System.out.println("all added " + counter.get());
        poller.start();
        poller.join(10 * 1000);
        qf.close();
    }
}
