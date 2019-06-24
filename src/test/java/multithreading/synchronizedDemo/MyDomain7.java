package multithreading.synchronizedDemo;

public class MyDomain7 {

    public void doLongTimeTask() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);

                System.out.println("nosynchronized threadName="
                        + Thread.currentThread().getName() + " i=" + (i + 1));
            }
            System.out.println("");
            synchronized (this) {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(1000);
                    System.out.println("synchronized threadName="
                            + Thread.currentThread().getName() + " i=" + (i + 1));
                }
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
