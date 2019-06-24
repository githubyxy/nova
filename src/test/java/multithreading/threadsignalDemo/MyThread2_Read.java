package multithreading.threadsignalDemo;

import java.io.PipedInputStream;

public class MyThread2_Read extends Thread {

    private ReadData2 read;
    private PipedInputStream input;

    public MyThread2_Read(ReadData2 read, PipedInputStream input) {
        super();
        this.read = read;
        this.input = input;
    }

    @Override
    public void run() {
        read.readMethod(input);
    }
}
