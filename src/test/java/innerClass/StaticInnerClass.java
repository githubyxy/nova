package innerClass;

/**
 * 静态内部类
 * 静态内部类也是定义在另一个类里面的类，只不过在类的前面多了一个关键字static。
 * 静态内部类是不需要依赖于外部类的，这点和类的静态成员属性有点类似，并且它不能使用外部类的非static成员变量或者方法
 *
 * @author yuxiaoyu
 */
public class StaticInnerClass {
    public static String a = "111";

    static {
        System.out.println(a);
    }

    /**
     * 静态内部类只有被调用的时候才会被加载
     *
     * @author yuxiaoyu
     */
    static class Inner {
        private static String b = "222";

        static {
            System.out.println(b);
        }

        public Inner() {

        }
    }

}
