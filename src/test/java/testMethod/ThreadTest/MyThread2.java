package testMethod.ThreadTest;

public class MyThread2 implements Runnable {

    @Override
    public void run() {
        try {
            Thread.currentThread().interrupt();        //  将线程中断标志位设为true
            for (int i = 0; i < 3; i++) {
                System.out.println("run = " + Thread.currentThread().getName());
                System.out.println("中断？ " + Thread.currentThread().interrupted());  // 返回当前线程中断标志位，然后设为false
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
