package multithreading.singtonDemo;

public class MySingletonObject2 {

    private static MySingletonObject2 instance;

    private MySingletonObject2() {

    }

    public static MySingletonObject2 getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            instance = new MySingletonObject2();
        }
        return instance;
    }
}
