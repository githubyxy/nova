package multithreading.threadsignalDemo;

import java.io.PipedOutputStream;

public class MyThread2_Write extends Thread {

    private WriteData2 write;
    private PipedOutputStream out;

    public MyThread2_Write(WriteData2 write, PipedOutputStream out) {
        super();
        this.write = write;
        this.out = out;
    }

    @Override
    public void run() {
        write.writeMethod(out);
    }

}
