package multithreading.threadsignalDemo;

import java.io.PipedWriter;

public class MyThread3_Write extends Thread {

    private WriteData3 write;
    private PipedWriter out;

    public MyThread3_Write(WriteData3 write, PipedWriter out) {
        super();
        this.write = write;
        this.out = out;
    }

    @Override
    public void run() {
        write.writeMethod(out);
    }

}
