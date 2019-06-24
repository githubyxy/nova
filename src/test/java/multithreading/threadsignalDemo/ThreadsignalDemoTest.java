package multithreading.threadsignalDemo;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;

import org.junit.Test;

public class ThreadsignalDemoTest {

    @Test
    public void test() throws InterruptedException {
        MyList service = new MyList();

        ThreadA a = new ThreadA(service);
        a.setName("A");
        a.start();

        ThreadB b = new ThreadB(service);
        b.setName("B");
        b.start();

        a.join();
        b.join();

    }

    /**
     * 等待通知机制
     * wait()释放锁，调用wait方法会立即释放，异常也会释放锁
     * notify()不释放锁，调用notify的同步方法内部，并不会立即释放锁，而是等执行完该同步代码块时，才会释放锁
     *
     * @throws InterruptedException
     */
    @Test
    public void test1() throws InterruptedException {
        Object object = new Object();
        MyThread1_1 myThread1_1 = new MyThread1_1(object);
        MyThread1_2 myThread1_2 = new MyThread1_2(object);
        myThread1_1.start();
        myThread1_2.start();

        myThread1_1.join();
        myThread1_2.join();

    }

    /**
     * 字节流通信
     */
    @Test
    public void test2() {
        try {
            WriteData2 writeData = new WriteData2();
            ReadData2 readData = new ReadData2();

            PipedInputStream inputStream = new PipedInputStream();
            PipedOutputStream outputStream = new PipedOutputStream();

            // inputStream.connect(outputStream);
            outputStream.connect(inputStream);

            MyThread2_Read threadRead = new MyThread2_Read(readData, inputStream);
            threadRead.start();

            Thread.sleep(2000);

            MyThread2_Write threadWrite = new MyThread2_Write(writeData, outputStream);
            threadWrite.start();

            threadRead.join();
            threadWrite.join();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 字符流
     */
    @Test
    public void test3() {
        try {
            WriteData3 writeData = new WriteData3();
            ReadData3 readData = new ReadData3();

            PipedReader inputStream = new PipedReader();
            PipedWriter outputStream = new PipedWriter();

            // inputStream.connect(outputStream);
            outputStream.connect(inputStream);

            MyThread3_Read threadRead = new MyThread3_Read(readData, inputStream);
            threadRead.start();

            Thread.sleep(2000);

            MyThread3_Write threadWrite = new MyThread3_Write(writeData, outputStream);
            threadWrite.start();

            threadRead.join();
            threadWrite.join();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
