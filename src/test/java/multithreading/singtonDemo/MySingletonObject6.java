package multithreading.singtonDemo;

/**
 * 枚举类实现单例模式
 * 枚举类不会被反射（反编译public final class T extends Enum，说明，该类是继承了Enum类的，同时final关键字告诉我们，这个类也是不能被继承的）
 * 枚举类不会序列化
 *
 * @author yuxiaoyu
 */
public enum MySingletonObject6 {

    A;

    private MyClass myClass;

    private MySingletonObject6() {
        myClass = new MyClass();
    }

    public MyClass getInstance() {
        return myClass;
    }

}
