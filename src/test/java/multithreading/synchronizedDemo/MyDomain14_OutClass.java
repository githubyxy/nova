package multithreading.synchronizedDemo;

public class MyDomain14_OutClass {

    static class Inner {
        public void method1() {
            synchronized ("other锁") {
                for (int i = 1; i <= 10; i++) {
                    System.out.println(Thread.currentThread().getName() + " i="
                            + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        public synchronized void method2() {
            for (int j = 0; j <= 10; j++) {
                System.out
                        .println(Thread.currentThread().getName() + " j=" + j);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    class Inner2 {
        public void method1() {
            synchronized ("other锁") {
                for (int i = 1; i <= 10; i++) {
                    System.out.println(Thread.currentThread().getName() + " i="
                            + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        public synchronized void method2() {
            for (int j = 0; j <= 10; j++) {
                System.out
                        .println(Thread.currentThread().getName() + " j=" + j);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
