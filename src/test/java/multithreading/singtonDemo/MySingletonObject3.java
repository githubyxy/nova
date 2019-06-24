package multithreading.singtonDemo;

public class MySingletonObject3 {

    private static volatile MySingletonObject3 instance;

    private MySingletonObject3() {

    }

    // 双检锁
    public static MySingletonObject3 getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            synchronized (MySingletonObject3.class) {
                if (instance == null) {
                    instance = new MySingletonObject3();
                }
            }
        }
        return instance;
    }
}
