package designpattern.singleton;

public class Singleton {

    private static boolean flag = false;
    private static volatile Singleton instance = null;

    private Singleton() {
        synchronized (Singleton.class) {
            if (false == flag) {
                flag = !flag;
            } else {
                throw new RuntimeException("单例模式正在被攻击");
            }

        }
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null)
                    instance = new Singleton();
            }
        }
        return instance;
    }
}
