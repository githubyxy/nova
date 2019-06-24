package designpattern.proxy;

public class Main {

    public static void main(String[] args) {
        Person person = new Person("美女");
        // 代理模式 访问的对象只与代理对象有联系，与被代理对象解耦
        Proxy proxy = new Proxy(person);

        proxy.GiveFlowers();
    }
}
