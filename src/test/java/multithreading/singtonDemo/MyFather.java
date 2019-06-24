package multithreading.singtonDemo;

public class MyFather {

    static {
        System.out.println("静态代码块");
    }

    {
        System.out.println("构造代码块");
    }

    private static String b = "static 私有成员变量";
    public static String b2 = "static 公有成员变量";

    private final String a = "final 私有 成员变量";
    public final String a2 = "final 公有 成员变量";
    private String name;
    private String age;

    public MyFather() {

    }


    public MyFather(String name, String age) {
        this.name = name;
        this.age = age;
    }

    private MyFather(String age) {
        this.age = age;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public static void staticMethod() {
        System.out.println("执行方法：staticMethod");
    }

    public void normalMethod() {
        System.out.println("执行方法：normalMethod");
    }

    private void privateMethod() {
        System.out.println("执行方法：privateMethod");
    }

    void method() {
        System.out.println("执行方法：method");
    }

    @Override
    public String toString() {
        return "MyFather [a=" + a + ", a2=" + a2 + ", b=" + b + ", b2=" + b2 + ", name=" + name + ", age=" + age + "]";
    }


}
