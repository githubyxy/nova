package multithreading.singtonDemo;

public class MySingletonObject1 {

    private static MySingletonObject1 instance = new MySingletonObject1();

    private MySingletonObject1() {

    }

    public static MySingletonObject1 getInstance() {
        return instance;
    }
}
