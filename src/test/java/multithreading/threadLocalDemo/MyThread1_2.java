package multithreading.threadLocalDemo;

public class MyThread1_2 extends Thread {

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Tools.tl.set("ThreadB" + (i + 1));
                System.out.println("ThreadB get Value=" + Tools.tl.get());
//				if (Tools.tl.get() == null) {
//					Tools.tl.set("ThreadB" + (i + 1));
//				} else {
//					System.out.println("ThreadB get Value=" + Tools.tl.get());
//				}
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
