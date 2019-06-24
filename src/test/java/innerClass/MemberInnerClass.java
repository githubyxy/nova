package innerClass;

/**
 * 成员内部类
 * 可以访问外部类的静态与非晶态的方法或者成员变量
 *
 * @author yuxiaoyu
 */
public class MemberInnerClass {
    static {
        System.out.println("123");
    }

    private double radius = 0;
    public static int count = 1;

    public MemberInnerClass(double radius) {
        this.radius = radius;
    }

    Draw getDrawInstance() {
        return new Draw();
    }

    /**
     * 内部类可以拥有private访问权限、protected访问权限、public访问权限及包访问权限。比如上面的例子，
     * 如果成员内部类Inner用private修饰，则只能在外部类的内部访问，如果用public修饰，则任何地方都能访问；
     * 如果用protected修饰，则只能在同一个包下或者继承外部类的情况下访问；如果是默认访问权限，则只能在同一个包下访问。
     * 这一点和外部类有一点不一样，外部类只能被public和包访问两种权限修饰
     *
     * @author yuxiaoyu
     */
    class Draw {     //内部类
        public void drawSahpe() {
            System.out.println(radius);  //外部类的private成员
            System.out.println(count);   //外部类的静态成员
        }
    }


}


