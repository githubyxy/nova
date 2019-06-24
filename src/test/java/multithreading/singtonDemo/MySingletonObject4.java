package multithreading.singtonDemo;

public class MySingletonObject4 {

    static {
        System.out.println("外部类静态块");
    }

    private MySingletonObject4() {

    }

    // 静态内部类
    /*
     * 静态内部类的方法也可以获取单例对象（反射会打破规则），而且它避免了外部类初始化时就加载对象的问题，内部类只会在调用的时候才被加载
     */
    private static class InnerClass {
        private static MySingletonObject4 instance = new MySingletonObject4();

        static {
            System.out.println("内部类静态块");
        }
    }

    //
    public static MySingletonObject4 getInstance() {
        return InnerClass.instance;
    }
}
