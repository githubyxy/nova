package multithreading.synchronizedDemo;

public class MyDomain15_OutClass {

    static class InnerClass1 {
        public void method1(InnerClass2 class2) {
            String threadName = Thread.currentThread().getName();
            synchronized (class2) {
                System.out.println(threadName + "进入InnerClass1类中method1方法" + System.currentTimeMillis());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {

                }
                System.out.println(threadName + "离开InnerClass1类中method1方法" + System.currentTimeMillis());
            }
        }

        public synchronized void method2() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + "进入InnerClass1类中method2方法" + System.currentTimeMillis());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {

            }
            System.out.println(threadName + "离开InnerClass1类中method2方法" + System.currentTimeMillis());
        }
    }

    static class InnerClass2 {
        public synchronized void method1() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + "进入InnerClass2类中method1方法" + System.currentTimeMillis());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {

            }
            System.out.println(threadName + "离开InnerClass2类中method1方法" + System.currentTimeMillis());
        }

        public synchronized static void method2() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + "进入InnerClass2类中method2方法" + System.currentTimeMillis());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {

            }
            System.out.println(threadName + "离开InnerClass2类中method2方法" + System.currentTimeMillis());
        }

        public void method3(InnerClass2 class2) {
            String threadName = Thread.currentThread().getName();
            synchronized (class2) {
                System.out.println(threadName + "进入InnerClass2类中method3方法" + System.currentTimeMillis());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {

                }
                System.out.println(threadName + "离开InnerClass2类中method3方法" + System.currentTimeMillis());
            }
        }

        public void method4() {
            synchronized (this) {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + "进入InnerClass2类中method4方法" + System.currentTimeMillis());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {

                }
                System.out.println(threadName + "离开InnerClass2类中method4方法" + System.currentTimeMillis());
            }
        }
    }
}
