package multithreading.threadLocalDemo;

import org.junit.Test;

public class ThreadLocalDemoTest {

    /**
     * 每个Thread都是维护着一个ThreadLocal.ThreadLocalMap
     * 这个ThreadLocalMap存放的 key=ThreadLocal对象，value就是保存的值
     * <p>
     * get()时，拿到当前线程的ThreadLocalMap，再根据key值（ThreadLocal对象）获取 ThreadLocalMap.Entry
     */
    @Test
    public void test1() {
        try {
            MyThread1_1 a = new MyThread1_1();
            MyThread1_2 b = new MyThread1_2();
            a.start();
            b.start();

            for (int i = 0; i < 100; i++) {
                if (Tools.tl.get() == null) {
                    Tools.tl.set("Main" + (i + 1));
                } else {
                    System.out.println("Main get Value=" + Tools.tl.get());
                }
                Thread.sleep(200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
