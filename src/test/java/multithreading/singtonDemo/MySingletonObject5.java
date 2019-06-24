package multithreading.singtonDemo;

import java.io.ObjectStreamException;
import java.io.Serializable;

public class MySingletonObject5 implements Serializable {

    private static final long serialVersionUID = 888L;

    // 内部类
    private static class MyObjectHandler {
        private static final MySingletonObject5 myObject = new MySingletonObject5();
    }

    private MySingletonObject5() {
    }

    public static MySingletonObject5 getInstance() {
        return MyObjectHandler.myObject;
    }

    protected Object readResolve() throws ObjectStreamException {
        System.out.println("调用重写的readResolve方法");
        return MyObjectHandler.myObject;
    }

}
