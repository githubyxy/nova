package multithreading.synchronizedDemo;

import org.junit.Test;

import multithreading.synchronizedDemo.MyDomain14_OutClass.Inner;
import multithreading.synchronizedDemo.MyDomain15_OutClass.InnerClass1;
import multithreading.synchronizedDemo.MyDomain15_OutClass.InnerClass2;

public class SynchronizedDemoTest {

    /**
     * 多个线程实例，访问同一个实例变量，内部实例变量共享
     * 非线程安全问题：多个线程对同一个对象中的同一个实例变量操作时
     * Mythread1 方法用synchronized修饰 可以解决线程非安全问题
     */
    @Test
    public void test() throws InterruptedException {
        MyDomain1 mythread1 = new MyDomain1();
        Mythread1_1 athread = new Mythread1_1(mythread1);
        athread.start();

        Mythread1_2 bthread = new Mythread1_2(mythread1);
        bthread.start();

        athread.join();
        bthread.join();
        System.out.println(Thread.currentThread().getName());
    }

    /**
     * 多个对象多个锁
     *
     * @throws InterruptedException
     */
    @Test
    public void test1() throws InterruptedException {
        MyDomain1 mythread1 = new MyDomain1();
        Mythread1_1 athread = new Mythread1_1(mythread1);
        athread.start();
        MyDomain1 mythread2 = new MyDomain1();
        Mythread1_2 bthread = new Mythread1_2(mythread2);
        bthread.start();

        athread.join();
        bthread.join();
        System.out.println(Thread.currentThread().getName());
    }

    /**
     * 1、A线程持有Object对象的Lock锁，B线程可以以异步方式调用Object对象中的非synchronized类型的方法
     * 2、A线程持有Object对象的Lock锁，B线程如果在这时调用Object对象中的synchronized类型的方法则需要等待，也就是同步
     *
     * @throws InterruptedException
     */
    @Test
    public void test2() throws InterruptedException {
        MyDomain2 object = new MyDomain2();
        Mythread2_1 a = new Mythread2_1(object);
        a.setName("A");
        Mythread2_2 b = new Mythread2_2(object);
        b.setName("B");

        a.start();
        b.start();
        a.join();
        b.join();
    }

    /**
     * 关键字synchronized拥有锁重入的功能。所谓锁重入的意思就是：当一个线程得到一个对象锁后，再次请求此对象锁时时可以再次得到该对象的锁的
     * 这种锁重入的机制，也支持在父子类继承的环境中。
     *
     * @throws InterruptedException
     */
    @Test
    public void test3() throws InterruptedException {
//		Mythread3 t = new Mythread3();
//		t.start();

        // 子类完全可以通过可重入锁调用父类的同步方法
        Mythread3_1 t = new Mythread3_1();
        t.start();

        t.join();
    }

    /**
     * 当一个线程执行的代码出现异常时，其所持有的锁会自动释放
     *
     * @throws InterruptedException
     */
    @Test
    public void test4() throws InterruptedException {
        MyDomain4 myDomain4 = new MyDomain4();
        Mythread4 a = new Mythread4(myDomain4);
        Mythread4 b = new Mythread4(myDomain4);
        a.start();
        b.start();
        a.join();
        b.join();
    }

    /**
     * 同步方法不能继承
     *
     * @throws InterruptedException
     */
    @Test
    public void test5() throws InterruptedException {
        MyDomain5_Son son = new MyDomain5_Son();

        Mythread5 a = new Mythread5(son);
        Mythread5 b = new Mythread5(son);
        a.start();
        b.start();
        a.join();
        b.join();
    }

    /**
     * 1、当A线程访问对象的synchronized代码块的时候，B线程依然可以访问对象方法中其余非synchronized块的部分，第一部分的执行结果证明了这一点
     * 2、当A线程进入对象的synchronized代码块的时候，B线程如果要访问这段synchronized块，那么访问将会被阻塞，第二部分的执行结果证明了这一点
     *
     * @throws InterruptedException
     */
    @Test
    public void test7() throws InterruptedException {
        MyDomain7 task = new MyDomain7();

        MyThread7_1 thread1 = new MyThread7_1(task);
        thread1.start();

        MyThread7_2 thread2 = new MyThread7_2(task);
        thread2.start();

        thread1.join();
        thread2.join();
    }

    /**
     * synchronized(this)块 获得的是一个对象锁，换句话说，synchronized块锁定的是整个对象。
     *
     * @throws InterruptedException
     */
    @Test
    public void test8() throws InterruptedException {
        MyDomain8 td = new MyDomain8();
        MyThread8_1 a = new MyThread8_1(td);
        MyThread8_2 b = new MyThread8_2(td);
        a.start();
        b.start();
        a.join();
        b.join();
    }

    /**
     * 1、synchronized同步方法
     * （1）对其他synchronized同步方法或synchronized(this)同步代码块呈阻塞状态
     * （2）同一时间只有一个线程可以执行synchronized同步方法中的代码
     * 2、synchronized(this)同步代码块
     * （1）对其他synchronized同步方法或synchronized(this)同步代码块呈阻塞状态
     * （2）同一时间只有一个线程可以执行synchronized(this)同步代码块中的代码
     *
     * @throws InterruptedException
     */
    @Test
    public void test9() throws InterruptedException {
        MyDomain9 td = new MyDomain9();
        MyThread9_1 a = new MyThread9_1(td);
        MyThread9_2 b = new MyThread9_2(td);
        a.start();
        b.start();
        a.join();
        b.join();
    }

    /**
     * synchronized(非this非自身其他对象x)代码块与synchronized方法呈非阻塞状态
     *
     * @throws InterruptedException
     */
    @Test
    public void test10() throws InterruptedException {
        MyDomain10 td = new MyDomain10();
        MyThread10_1 a = new MyThread10_1(td);
        MyThread10_2 b = new MyThread10_2(td);
        a.start();
        b.start();
        a.join();
        b.join();
    }

    /**
     * synchronized(非this自身对象x)代码块与synchronized方法或synchronized(this)都呈阻塞状态
     * test10 与test11 也能证明 synchronized方法与synchronized(this) 持有的是对象锁
     *
     * @throws InterruptedException
     */
    @Test
    public void test11() throws InterruptedException {
        MyDomain11 td = new MyDomain11();
        MyThread11_1 a = new MyThread11_1(td);
        MyThread11_2 b = new MyThread11_2(td);
        a.start();
        b.start();
        a.join();
        b.join();
    }

    /**
     * synchronized静态方法也是互斥的（printA和printB可以看出）
     * synchronized静态方法与synchronized方法持有的是不同的锁（printC()方法的调用和对printA()方法、printB()方法的调用时异步的）
     * synchronized静态方法持有的是对当前.java文件对应的Class类加锁
     *
     * @throws InterruptedException
     */
    @Test
    public void test12() throws InterruptedException {
        MyDomain12 md = new MyDomain12();
        Mythread12_1 mt1 = new Mythread12_1();
        Mythread12_2 mt2 = new Mythread12_2();
        Mythread12_3 mt3 = new Mythread12_3(md);
        mt1.start();
        mt2.start();
        mt3.start();

        mt1.join();
        mt2.join();
        mt3.join();
    }

    /**
     * 死锁
     * 1.在jdk下 执行jps命令获取当前死锁程序的pid
     * 2.jstack -l pid 查看死锁原因
     *
     * @throws InterruptedException
     */
    @Test
    public void test13() throws InterruptedException {
        Mythread13 mythread13 = new Mythread13();
        Thread a = new Thread(mythread13);
        Thread b = new Thread(mythread13);
        a.start();
        b.start();
        a.join();
        b.join();
    }

    @Test
    public void test14() throws InterruptedException {
//		final Inner inner = new Inner();
        final MyDomain14_OutClass.Inner2 inner = new MyDomain14_OutClass().new Inner2();

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                inner.method1();
            }
        }, "A");

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                inner.method2();
            }
        }, "B");

        t1.start();
        t2.start();


        t1.join();
        t2.join();
    }

    /**
     * 在同一个内部类中，synchronized方法 synchronized静态方法 synchronized(this) 效果同一般的类
     * 而在一个类的不同内部类中,synchronized(class2) 与 class2中的synchronized静态方法 是非阻塞的（可以同时进入方法）,而对于其他同步方法都是阻塞的
     *
     * @throws InterruptedException
     */
    @Test
    public void test15() throws InterruptedException {
        final InnerClass1 in1 = new InnerClass1();
        final InnerClass2 in2 = new InnerClass2();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                in1.method1(in2);
            }
        }, "T1");
        Thread t2 = new Thread(new Runnable() {
            public void run() {
//				in1.method2();
            }
        }, "T2");

        Thread t3 = new Thread(new Runnable() {
            public void run() {
                in2.method1();
            }
        }, "T3");

        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                in2.method2();
            }
        }, "T4");
        Thread t5 = new Thread(new Runnable() {
            @Override
            public void run() {
                in2.method3(in2);
            }
        }, "T5");
        Thread t6 = new Thread(new Runnable() {
            @Override
            public void run() {
                in2.method4();
            }
        }, "T6");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
    }


}
