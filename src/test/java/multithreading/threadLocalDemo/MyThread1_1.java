package multithreading.threadLocalDemo;

public class MyThread1_1 extends Thread {

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Tools.tl.set("ThreadA" + (i + 1));
                System.out.println("ThreadA get Value=" + Tools.tl.get());
//				if (Tools.tl.get() == null) {
//					Tools.tl.set("ThreadA" + (i + 1));
//				} else {
//					System.out.println("ThreadA get Value=" + Tools.tl.get());
//				}
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
