package multithreading.threadsignalDemo;

import java.io.PipedReader;

public class MyThread3_Read extends Thread {

    private ReadData3 read;
    private PipedReader input;

    public MyThread3_Read(ReadData3 read, PipedReader input) {
        super();
        this.read = read;
        this.input = input;
    }

    @Override
    public void run() {
        read.readMethod(input);
    }
}
