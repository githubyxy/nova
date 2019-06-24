package designpattern.singleton;

public enum SingletonEnum {

    INSTANCE;

    private Singleton instance;

    SingletonEnum() {
        instance = Singleton.getInstance();
    }

    public Singleton getInstance() {
        return instance;
    }

}
